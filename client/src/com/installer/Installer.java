package com.installer;

import com.minghao.ErrorFrame;

public class Installer {

    private final static ErrorFrame errorFrame = new ErrorFrame();

    public static void main(String arg[]) {
        new InstallFrame(errorFrame);
    }
}