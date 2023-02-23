package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class Marshaller {

    /* This segment is just for testing the Marshaller and Unmarshaller. To be deleted later on */
    public static void main(String[] args) throws Exception {
        Marshaller m = new Marshaller();

        HashMap<String, Object> message = new HashMap<String, Object>();
        message.put("Flight Number", Integer.valueOf(123));
        message.put("Message", "Long flight message details here");
        message.put("Cost", "0.54");

        ArrayList<Integer> intarr = new ArrayList<Integer>();
        intarr.add(10);
        intarr.add(5);
        intarr.add(15);
        message.put("Sample flight list", intarr);

        ArrayList<String> strarr = new ArrayList<String>();
        strarr.add("Taiwan");
        strarr.add("South Korea");
        message.put("Sample Destinations", strarr);

        byte[] res = m.marshal(message);
        message = m.unmarshal(res);
        System.out.println(message);
        for(String field : message.keySet()){
            System.out.println(field);
            System.out.println(message.get(field));
            if(message.get(field) instanceof ArrayList){
                ArrayList temp = (ArrayList) message.get(field);
                System.out.println(temp.get(0));
            }
            System.out.println();
        }
    }
    
    public Marshaller(){}

    private String formatStringArray(ArrayList<String> strarr) {
        StringJoiner sj = new StringJoiner("]]]");
        for(String s: strarr) {
            sj.add(s);
        }
        return sj.toString();
    }

    private ArrayList<String> parseSTringArray(String strarr) {
        ArrayList<String> res = new ArrayList<String>();

        String[] temp = strarr.split("]]]");
        for(String s: temp){
            res.add(s);
        }

        return res;
    }

    private String formatIntegerArray(ArrayList<Integer> intarr) {
        StringJoiner sj = new StringJoiner("]]]");
        for(Integer i: intarr) {
            sj.add(Integer.toString(i));
        }
        return sj.toString();
    }

    private ArrayList<Integer> parseIntegerArray(String intarr) {
        ArrayList<Integer> res = new ArrayList<Integer>();

        String[] temp = intarr.split("]]]");
        for(String s: temp){
            res.add(Integer.parseInt(s));
        }

        return res;
    }

    /*
     * flight identifier (integer),
     * the source and destination places (variable-length strings),
     * the departure time (design your own data structure to represent time), 
     * the airfare (floating-point value), and the seat availability (an integer indicating the number of seats available). 
     */
    public byte[] marshal(HashMap<String, Object> message){
        byte[] res = new byte[1024];
        int pointer = 0;
        
        StringJoiner joiner;
        for (String field : message.keySet()) {
            // Use this to dump the byte array into text for testing
            // for(int i=0; i< res.length ; i++) {
            //     System.out.print(res[i] +" ");
            // }
            // System.out.println();
            joiner =  new StringJoiner("@@@");

            joiner.add(field);

            String type = "";
            String content = "";
            if (message.get(field) instanceof Integer) {
                type = "Integer";
                content = message.get(field).toString();
            } else if (message.get(field) instanceof String) {
                type = "String";
                content = message.get(field).toString();
            } else if (message.get(field) instanceof Float) {
                type = "Float";
                content = message.get(field).toString();
            } else if (message.get(field) instanceof ArrayList){
                ArrayList tempArrayList = (ArrayList) message.get(field);

                if(tempArrayList.get(0) instanceof Integer) {
                    type = "IntegerArray";
                    content = formatIntegerArray(tempArrayList);
                } else if(tempArrayList.get(0) instanceof String) {
                    type = "StringArray";
                    content = formatStringArray(tempArrayList);
                }
                
            }
            joiner.add(type);
            joiner.add(content);

            String joinedString = joiner.toString();
            byte[] tempBuf = joinedString.getBytes();
            byte[] byteSize = ByteBuffer.allocate(4).putInt(tempBuf.length).array();

            System.arraycopy(byteSize, 0, res, pointer, 4);
            pointer += 4;
            System.arraycopy(tempBuf, 0, res, pointer, tempBuf.length);
            pointer += tempBuf.length;
        }

        return res;
    }

    public HashMap<String, Object> unmarshal(byte[] message) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        
        int pointer = 0;
        byte[] buf;
        byte[] messageSize;
        while(pointer < message.length) {
            messageSize = new byte[4];
            buf = new byte[1024];
            System.arraycopy(message, pointer, messageSize, 0, 4);
            pointer += 4;
            
            int value = 0;
            for (byte b : messageSize) {
                value = (value << 8) + (b & 0xFF);
            }
            
            if(value == 0){
                break;
            }

            System.arraycopy(message, pointer, buf, 0, value);
            pointer += value;
            String combined = new String(buf, 0, value);
            String[] temp = combined.split("@@@");
            
            String field = temp[0];
            String type = temp[1];
            String content = temp[2];
            
            if (type.equals("Integer")) {
                res.put(field, Integer.valueOf(content));
            } else if (type.equals("Float")) {
                res.put(field, Float.valueOf(content));
            } else if (type.equals("String")) {
                res.put(field, content);
            } else if (type.equals("IntegerArray")) {
                res.put(field, parseIntegerArray(content));
            } else if (type.equals("StringArray")) {
                res.put(field, parseSTringArray(content));
            }
        }

        return res;
    }
}
