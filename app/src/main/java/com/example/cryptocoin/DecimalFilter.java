package com.example.cryptocoin;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DecimalFilter implements InputFilter {

    private Pattern mPattern;

    public DecimalFilter (int digitsAfterDecimal) {
        mPattern = Pattern.compile("(([1-9]{1}[0-9]{0,})?||[0]{1})((\\,[0-9]{0," + digitsAfterDecimal + "})?)||(\\,)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {

        String newString =
                dest.toString().substring(0, dstart) + source.toString().substring(start, end)
                        + dest.toString().substring(dend, dest.toString().length());

        Matcher matcher = mPattern.matcher(newString);
        if (!matcher.matches()) {
            return "";
        }
        return null;
    }
}
