package com.installer;


import java.io.IOException;

public class Installer{

    Installer(){
        try {
            new InstallFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



     public static void main(String arg[]){
         new Installer();
     }
}
