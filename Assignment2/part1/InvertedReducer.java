package stubs;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IndexReducer extends Reducer<Text, Text, Text, Text> {

  private Text result = new Text();

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {

    StringBuilder sb = new StringBuilder();

    for (Text val : values) {
      sb.append(val.toString()).append("\n");
    }

    result.set(sb.toString().trim());
    context.write(key, result);
  }
}
