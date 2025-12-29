package stubs;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {
  public static void main(String[] args) throws Exception {
    args = new String[]{
      "/home/training/training_materials/analyst/data/ratings_2012.txt",
      "/user/training/output"
    };

    FileUtils.deleteDirectory(new File("output"));  // for local testing, ignored in HDFS

    System.setProperty("hadoop.home.dir", "/usr/lib/hadoop"); // adjust if needed

    if (args.length < 2) {
      System.err.println("Please specify the input and output path");
      System.exit(-1);
    }

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf);
    job.setJarByClass(InvertedIndex.class);
    job.setJobName("Find Specific Comments");

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setMapperClass(IndexMapper.class);
    job.setReducerClass(IndexReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
