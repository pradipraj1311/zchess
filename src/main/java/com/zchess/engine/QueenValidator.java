package com.zchess.engine;

public class QueenValidator {

    public static boolean isValid(int fr,int fc,int tr,int tc){

            return fr==tr || fc==tc ||
                           Math.abs(fr-tr)==Math.abs(fc-tc);

                               }

                               }