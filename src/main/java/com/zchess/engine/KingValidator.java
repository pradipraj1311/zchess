package com.zchess.engine;

public class KingValidator {

    public static boolean isValid(int fr,int fc,int tr,int tc){

            return Math.abs(fr-tr)<=1 &&
                           Math.abs(fc-tc)<=1;

                               }

                               }