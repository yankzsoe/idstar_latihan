package com.synrgy.commit.util;

public class Test {
    public static void main(String[] args) {

    }

    public void mehtod1(){
        System.out.println("method 1");
    }

    public void mehtod1(String nama){
        System.out.println("method 2 dengan parameter string"+nama);
    }

    public String mehtod1(String nama, String alamat){
        System.out.println("method  return 3 dengan 2 parameter string  "+nama + alamat);
        return nama +alamat;
    }
}
