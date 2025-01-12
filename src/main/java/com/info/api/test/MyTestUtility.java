package com.info.api.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class MyTestUtility {


    public static void main(String[] args) throws IOException {
        springJoinerExample();
        validateCharOnly();
        System.out.println(verifyRoutingNo("346534664"));
        System.out.println(verifyRoutingNo("3465346645"));
//        getPath();
        String str = "504  NORTH SHAHJAHANPUR DHAKA 1217 DGM PRINCIPAL OFFICE  NARAYANGANJ ";
        System.out.println(str.substring(0, 35));
        System.out.println(str.substring(0, 36));
        System.out.println(str.subSequence(0, 69));
        System.out.println("abcd".substring(0, 3));
//        switchExample(STATUS_COMPLETED);
//        apiCall();
        System.out.println(generateBase64Hash("AE90009999", "Qwe_123!"));
//        System.out.println(UUID.randomUUID());
        IntSummaryStatistics intSummaryStatistics = IntStream.range(301, 400).summaryStatistics();
        Spliterator.OfInt spliterator = IntStream.range(301, 400).spliterator();

        List<Long> collect = LongStream.rangeClosed(300, 399).flatMap(d -> Arrays.stream(String.valueOf(d).split(""))
                .mapToLong(Long::new)).boxed().filter(d -> d == 3).collect(Collectors.toList());
        System.out.println(collect.stream().count());
        System.out.println();
        getThree();

//        LongStream generate = LongStream.generate(() -> {
//            System.out.println("hg\n");
//            return (long) (Math.random() * 10000);
//        });
//        System.out.println(generate.max());
//        generate.limit(4).forEach(System.out::println);
    }

    public static void springJoinerExample(){
        List<String> strings = new ArrayList<>(Arrays.asList("1", "2", "3"));
        StringJoiner joinNames = new StringJoiner(",");
        String result = strings.stream().collect(Collectors.joining(", ", "[", "]"));
        String s = "('" + String.join("','", strings) + "')";
        System.out.println(s);
    }
    public static void validateCharOnly(){
        String text = "a @5b";
        String number;
        String digit = "^.*[0-9]+.*$";
        String digits = "\\d*";
        String character = "^.*[a-zA-Z0-9]+.*$";
        String characters = "^[a-zA-Z]+$";
        String creditorName  = "fsdf@s".replace(" ", "");
        Pattern special = Pattern.compile ("[!@#$%^&*()+=|<>?{}\\[\\]~]");
        if(!special.matcher(creditorName).find() && !creditorName.matches(digits)){
            System.out.println(true);
        }

    }
    private static boolean verifyRoutingNo(String routingNumber) {
        boolean isValid = false;
        try {
            if (routingNumber.length() == 9) {
                isValid = true;
            }
            if (isValid) {
                Integer.parseInt(routingNumber);
                isValid = true;
            }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    static void getThree() {
        int count = 0;
        for (int i = 300; i <= 399; i++) {
            String[] nums = String.valueOf(i).split("");
            for (int j = 0; j < nums.length; j++) {
                if (Integer.parseInt(nums[j]) == 3) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    public static String generateBase64Hash(String userId, String password) {
        String originalInput = userId + ":" + password;
        return Base64.getEncoder().encodeToString(originalInput.getBytes());
    }

    public static void switchExample(String status) {
        int number = 20;

        switch (number) {
            //Case statements
            case 10:
                System.out.println("10");
                break;
            case 20:
                System.out.println("20");
                break;
            case 30:
                System.out.println("30");
                break;
            //Default case statement
            default:
                System.out.println("Not in 10, 20 or 30");
        }
//        String newStatus = NEW_STATUS_D;
//        switch (status) {
//            case STATUS_COMPLETED:
//                newStatus = NEW_STATUS_Y;
//                break;
//            case STATUS_REJECTED:
//                newStatus = NEW_STATUS_X;
//                break;
//            default:
//                newStatus = NEW_STATUS_D;
//        }
//        System.out.println(newStatus);
    }


//    public static void apiCall() throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://api.instantcashworldwide.ae/sandbox/api/v1/transactions/receive-payment")
//                .get()
//                .addHeader("accept", "application/json")
//                .addHeader("x-fapi-financial-id", "AE01BH")
//                .addHeader("Authorization", "Basic QUU5MDAwNTU1NTpRd2VfMTIzIQ==")
//                .addHeader("Ocp-Apim-Subscription-Key", "769f2166defa44789a3a7e42edf110d4")
//                .addHeader("x-correlation-id", UUID.randomUUID().toString())
//                .addHeader("x-idempotency-key", UUID.randomUUID().toString())
//                .build();
//
////        Response response = client.newCall(request).execute();
////        System.out.println(response);
//    }

    public static void getPath() {
        String srcPath = "/DEV_BRANCH/PanaceaUpdate/panacea/EFT/Update/pack8/pacs.008.xml";

        // try-catch block to handle exceptions
        try {
            // Create a file object
            File file = new File(srcPath);
            file = new File(new URI("file:///Users/pankaj/test.txt"));
            // Get the absolute path of file f
            String absolute = file.getAbsolutePath();
            // Display the file path of the file object and also the file path of absolute file
            System.out.println("Original  path: " + file.getPath());
            System.out.println("Absolute  path: " + absolute);

            String relativePath = srcPath;
            Path absolutePath = Paths.get(relativePath).toAbsolutePath();
            System.out.println("Canonical Path: " + file.getCanonicalPath());
            System.out.println("Path: " + file.getPath());
            Path pathAbsolute = Paths.get(srcPath);
            Path pathBase = Paths.get("/panacea/EFT");
            Path pathRelative = pathBase.relativize(pathAbsolute);
            System.out.println("pathRelative: " + pathRelative);
        } catch (Exception e) {
            System.err.println("Absolute  path error: " + e.getMessage());
        }
    }

    public static void mainn(String[] args) throws IOException, URISyntaxException {
        encoder("Bangladesh");
        encid();
        String srcPath = "/DEV_BRANCH/PanaceaUpdate/panacea/EFT/Update/pack8/pacs.008.xml";
        File file = new File(srcPath);
        printPaths(file);
        // relative path
        file = new File("/panacea/EFT/Update/pack8/pacs.008.xml");
        printPaths(file);
        // complex relative paths
        file = new File("/Users/pankaj/../pankaj/test.txt");
//        printPaths(file);
        // URI paths
        file = new File(new URI("file:///panacea/EFT/Update/pack8/pacs.008.xml"));
        printPaths(file);
    }

    private static void printPaths(File file) throws IOException {
        System.out.println("Absolute Path: " + file.getAbsolutePath());
        System.out.println("Canonical Path: " + file.getCanonicalPath());
        System.out.println("Path: " + file.getPath());
    }

    static void encoder(String password) {
        String encode = Base64.getEncoder().encodeToString(password.getBytes());
        String decode = Arrays.toString(Base64.getDecoder().decode(encode));
        System.out.println(String.format("%02x", encode));
    }

    public static void encid() {
        String pText = "Hello MD5";
        byte[] md5InBytes = digest(pText.getBytes(UTF_8));
        String hash = bytesToHex(md5InBytes);
        System.out.println(hash);
        System.out.println(String.format(OUTPUT_FORMAT, "MD5 (hex) ", bytesToHex(md5InBytes)));
    }

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String OUTPUT_FORMAT = "%-20s:%s";

    private static byte[] digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        try {
            String s = "";
        } finally {

        }
        return sb.toString();
    }

//    public static void test() {
//        final int i;
//        i = 0;
//        int j = i + 20;
//        i = j + 30;
////        final int i;
//        i = 20;
//        int j = i + 20;
//        i = j + 30;
//        System.out.println(i + " " + j);
//
//    }
}
