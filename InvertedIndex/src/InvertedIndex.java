import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class InvertedIndex {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
			if(args.length != 2) {
				System.err.println("Usage: Inverted Index <input path> <output path>");
				System.exit(-1);
			}

			Job job = new Job();
			job.setJarByClass(InvertedIndex.class);
			job.setJobName("Inverted Index");
			
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
				
			job.setMapperClass(InvertedIndexMapper.class);
			job.setReducerClass(InvertedIndexReducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			job.waitForCompletion(true);
	}
}

class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

	private Text word = new Text();
	private Text documentID = null;

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line);
			String docID = ((FileSplit) context.getInputSplit()).getPath().getName();

			while(tokenizer.hasMoreTokens()) { 
				word.set(tokenizer.nextToken());
				documentID = new Text(docID);
				context.write(word, documentID);
			}
	}
}

class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			HashMap endResult = new HashMap();
			int totalOfWords = 0;
			for(Text tempText : values) {
				String holdValue = tempText.toString();
				// Assign the word count of each file per word
				if(endResult != null && endResult.get(holdValue)!=null) {
					endResult.put(holdValue, (int)endResult.get(holdValue)+1);
					totalOfWords += 1;
				} else {
					endResult.put(holdValue, 1);
					totalOfWords += 1;
				}
			}

			String wholeTable = totalOfWords + "," + endResult.toString();
			Text tableText = new Text(wholeTable);
			context.write(key, tableText);
	}
}
