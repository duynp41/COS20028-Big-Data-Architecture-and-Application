package stubs;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IndexMapper extends Mapper<Object, Text, Text, Text> {

  private Text outputKey = new Text();
  private Text outputValue = new Text();
  private String filename;

  private static final String TARGET_1 = "Item was defective";
  private static final String TARGET_2 = "Shoddy";

  private int lineNumber = 0;

  @Override
  protected void setup(Context context) {
    FileSplit fileSplit = (FileSplit) context.getInputSplit();
    filename = fileSplit.getPath().getName();
  }

  @Override
  public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    lineNumber++;
    String line = value.toString().trim();
    if (line.isEmpty()) return;

    String[] fields = line.split("\t");
    if (fields.length < 5) return;

    String comment = fields[4].trim();
    boolean isTarget =
      comment.equalsIgnoreCase(TARGET_1) || comment.equalsIgnoreCase(TARGET_2);

    // Only match "Shoddy" if it's a single-word comment
    if (isTarget && !(comment.equalsIgnoreCase(TARGET_2) && comment.split(" ").length > 1)) {
      outputKey.set(comment);
      outputValue.set(filename + ":" + lineNumber + " | " + line);
      context.write(outputKey, outputValue);
    }
  }
}
