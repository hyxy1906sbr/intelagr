package com.oracle.intelagr.common;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public int cal(int[] is){
        int n = 0;
        int many = is[is.length-1];
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for(int i=0;i<is.length-1;i++){
            arr.add(is[i]);
        }
        Collections.sort(arr);
        int start = 0;
        int end = arr.size()-1;
        int mid = (start+end)/2;
        int flag = 0;
        while(start < end){
            if(arr.get(mid)>many){
                end = mid-1;
                flag = 1;
            }
            else if(arr.get(mid)<many){
                start = mid+1;
                flag = 0;
            }
            else{
                return mid;
            }
            mid = (start+end)/2;
        }
        mid += flag;
        int temp = 0;
        temp = many - mid;

        return n;
    }

}
