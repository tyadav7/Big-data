package com.java.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import com.v2maestros.spark.bda.common.SparkConnection;

import scala.Tuple2;



public class SimpleEdu {
	
	public static void main(String args[])
	{
		JavaSparkContext spContext = SparkConnection.getContext();
		JavaRDD<String> rdd1 = spContext.textFile("data/student_consolidated_report.txt");
		JavaRDD<String> studentResponse = spContext.textFile("data/student_response.txt");
		JavaRDD<String> questionData = spContext.textFile("data/question_data.txt");
		String header = rdd1.first();
		rdd1 = rdd1.filter(s -> !s.equals(header));
		String header2 = studentResponse.first();
		studentResponse = studentResponse.filter(s -> !s.equals(header2));
		String header3 = questionData.first();
		rdd1 = rdd1.filter(s -> !s.equals(header3));
		
		Function<String, StudentConsolidatedReport> mapper = new Function<String, StudentConsolidatedReport>() {

			@Override
			public StudentConsolidatedReport call(String v1) throws Exception {
				String[] data = v1.split("\\|");
				StudentConsolidatedReport studentReport = new StudentConsolidatedReport();
				studentReport.setStudentId(Integer.parseInt(data[0]));
				studentReport.setQuestionSet(data[1]);
				studentReport.setQT(Integer.parseInt(data[2]));
				studentReport.setVB(Integer.parseInt(data[3]));
				studentReport.setRA(Integer.parseInt(data[4]));
				studentReport.setQuestionAttempted(Integer.parseInt(data[5]));
				studentReport.setAttemptDate(data[5]);
				return studentReport;
			}
			
		};
		
		Function<String, StudentResponse> studentResponseMapper = new Function<String, StudentResponse>() {

			@Override
			public StudentResponse call(String v1) throws Exception {
				String[] data = v1.split("\\|");
				StudentResponse studentResponse = new StudentResponse();
				studentResponse.setStudentID(Integer.parseInt(data[0]));
				studentResponse.setQuestionSet(data[1]);
				studentResponse.setSection(data[2]);
				studentResponse.setqID(data[3]);
				studentResponse.setAnswered(data[4].toCharArray()[0]);
				if(!data[5].equals("")) {
					studentResponse.setResult(data[5].toCharArray()[0]);
				}
				studentResponse.setTimeTaken(data[6]);
				studentResponse.setAttemptTS(data[7]);
				return studentResponse;
			}
			
		};
		
		Function<String, QuestionData> questionDataMapper = new Function<String, QuestionData>() {

			@Override
			public QuestionData call(String v1) throws Exception {
				String[] data = v1.split("\\|");
				QuestionData questionData = new QuestionData();
				questionData.setqID(data[0]);
				questionData.setQdesc(data[1]);
				questionData.setSection(data[2]);
				questionData.setCategory(data[3]);
				questionData.setDiffLevel(data[4]);
				questionData.setCreatedBy(data[5]);
				questionData.setCreatedDate(data[6]);
				return questionData;
			}
		};
		
		JavaRDD<StudentConsolidatedReport> studentRDD = rdd1.map(mapper);
		JavaRDD<StudentResponse> studentResponseRDD = studentResponse.map(studentResponseMapper);
		JavaRDD<QuestionData> questionDataRDD = questionData.map(questionDataMapper);
		
		JavaRDD<Integer> qtSorted = studentRDD.map(t -> t.getQT()).sortBy( t -> t , true , 1);
		JavaRDD<Integer> raSorted = studentRDD.map(t -> t.getRA()).sortBy( t -> t , true , 1);
		JavaRDD<Integer> vbSorted = studentRDD.map(t -> t.getVB()).sortBy( t -> t , true , 1);
		
		int totalRecords = (int) Math.round(qtSorted.count()*0.2); 
		double avgQT = (qtSorted.top(totalRecords).stream().reduce( (a , b) -> a + b ).orElse(0))/totalRecords;
		double avgRA = (raSorted.top(totalRecords).stream().reduce( (a , b) -> a + b ).orElse(0))/totalRecords;
		double avgVB = (vbSorted.top(totalRecords).stream().reduce( (a , b) -> a + b ).orElse(0))/totalRecords;
		
		List<Output> outputs = new ArrayList();
		Output output = new Output();
		output.setQuestionSet("tt503");
		output.setSection("QT");
		output.setAvgMarks(avgQT);
		outputs.add(output);
		
		output = new Output();
		output.setQuestionSet("tt503");
		output.setSection("RA");
		output.setAvgMarks(avgRA);
		outputs.add(output);
		
		output = new Output();
		output.setQuestionSet("tt503");
		output.setSection("VB");
		output.setAvgMarks(avgVB);
		outputs.add(output);
		
		JavaRDD<StudentConsolidatedReport> lessQT = studentRDD.filter( t -> t.getQT() < avgQT);
		JavaRDD<StudentConsolidatedReport> lessVB = studentRDD.filter( t -> t.getVB() < avgVB);
		JavaRDD<StudentConsolidatedReport> lessRA = studentRDD.filter( t -> t.getRA() < avgRA);

		PairFunction<StudentResponse, String , Integer> pairFunc = new PairFunction<StudentResponse, String, Integer>() {

			@Override
			public Tuple2<String, Integer> call(StudentResponse t) throws Exception {
				
				int value = 0;
				if(t.getAnswered() == 'N') {
					value = 1;
				}
				return new Tuple2<String, Integer>(t.getSection(), value);
			}
		};
		
		JavaPairRDD<String, Integer> unansweredPairRDD = studentResponseRDD.mapToPair(pairFunc);
		unansweredPairRDD = unansweredPairRDD.reduceByKey((a , b) -> a+b);
		JavaPairRDD<String, Integer> wronglyAnsweredPairRDD = studentResponseRDD.mapToPair(t -> {
			int value = 0;
			if(t.getResult() == 'W') {
				value = 1;
			}
			return new Tuple2<String, Integer>(t.getSection(), value);
		});
		
		wronglyAnsweredPairRDD = wronglyAnsweredPairRDD.reduceByKey((a , b) -> a+b);
		JavaPairRDD<String , Integer> allWrongAndUnaswered = wronglyAnsweredPairRDD.union(unansweredPairRDD).reduceByKey( (a ,b) -> a + b);
		System.out.println(allWrongAndUnaswered.take((int) allWrongAndUnaswered.count()));
		
		questionDataRDD = questionDataRDD.filter(q -> {
			if(q.getDiffLevel().equals("VeryDifficult")||q.getDiffLevel().equals("Difficult")) 
			{
				return false;
			}
			return true;
		});
		
		JavaPairRDD<String, Iterable<QuestionData>> categoryVSQuestion = questionDataRDD.groupBy(t -> t.getCategory());
		
		Function<Tuple2<String, Iterable<QuestionData>> , Tuple2<String, Iterable<QuestionData>>> function = new Function<Tuple2<String, Iterable<QuestionData>> , Tuple2<String, Iterable<QuestionData>>>() {

			@Override
			public Tuple2<String, Map<String , QuestionData>> call(Tuple2<String, Map<String , QuestionData>> v1) throws Exception {
				
				List<QuestionData> easyQuestions = new ArrayList<>();
				List<QuestionData> medQuestions = new ArrayList<>();
				Map<String , QuestionData> map = new HashMap<>();
				int medCount = 0;
				int easyCount = 0;
				
				for(QuestionData questionData: v1._2) 
				{
					if(questionData.getDiffLevel().equals("Medium") && medCount < 5) 
					{
							medQuestions.add(questionData);
							map.put("Medium" , medQuestions);
							medCount++;
					}
						
					else if(questionData.getDiffLevel().equals("Easy") && easyCount < 5) 
					{
						easyQuestions.add(questionData);
						map.put("Easy" , easyQuestions);
						easyCount++;
					}
				}
				
				return new Tuple2<String, Iterable<QuestionData>>(v1._1 , map);
				
			}
		};
		
		JavaRDD<Tuple2<String, Iterable<QuestionData>>> categoryVSQuestions = categoryVSQuestion.map(function);
		
		JavaRDD<StudentConsolidatedReport> studentQTRDD = studentRDD.filter( t -> t.getQT() > avgQT);
		JavaRDD<StudentConsolidatedReport> studentVBRDD = studentRDD.filter( t -> t.getVB() > avgVB);
		JavaRDD<StudentConsolidatedReport> studentRARDD = studentRDD.filter( t -> t.getRA() > avgRA);
		
		
	}
}
