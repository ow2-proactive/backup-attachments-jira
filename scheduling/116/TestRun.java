import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;


public class TestRun {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("------------- Testing with String -------------");
        String testString = "/bin/bash -c echo toto;echo tata";
        System.out.println("Tokenization:");
        System.out.println(tokenize(testString));
        System.out.println("Running:");
        {
            Process p = Runtime.getRuntime().exec(testString);
            BufferedReader serr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = serr.readLine()) != null) {
                System.out.println(line);
            }
            serr.close();

            p.waitFor();

            System.out.println(p.exitValue());
        }

        System.out.println();

        System.out.println("------------- Testing with String[] ------------------");
        System.out.println("Running:");
        String[] testStringArray = new String[]{"/bin/bash", "-c", "echo toto;echo tata"};

        {
            Process p = Runtime.getRuntime().exec(testStringArray);
            BufferedReader serr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = serr.readLine()) != null) {
                System.out.println(line);
            }
            serr.close();

            p.waitFor();

            System.out.println(p.exitValue());
        }

        System.out.println("------------- Testing with String and double quotes -------------");
        String testString2 = "/bin/bash -c \"echo toto;echo tata\"";
        System.out.println("Tokenization:");
        System.out.println(tokenize(testString2));
        System.out.println("Running:");
        {
            Process p = Runtime.getRuntime().exec(testString2);
            BufferedReader serr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = serr.readLine()) != null) {
                System.out.println(line);
            }
            serr.close();

            p.waitFor();

            System.out.println(p.exitValue());
        }
        System.out.println();

        System.out.println("------------- Testing with String and simple quotes -------------");
        String testString3 = "/bin/bash -c 'echo toto;echo tata'";
        System.out.println("Tokenization:");
        System.out.println(tokenize(testString3));
        System.out.println("Running:");
        {
            Process p = Runtime.getRuntime().exec(testString3);
            BufferedReader serr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = serr.readLine()) != null) {
                System.out.println(line);
            }
            serr.close();

            p.waitFor();

            System.out.println(p.exitValue());
        }
        System.out.println();

    }

    public static ArrayList<String> tokenize(String arg) {
        StringTokenizer tizer = new StringTokenizer(arg);
        ArrayList<String> ans = new ArrayList<String>(tizer.countTokens());
        while (tizer.hasMoreTokens()) {
            ans.add(tizer.nextToken());
        }
        return ans;
    }
}
