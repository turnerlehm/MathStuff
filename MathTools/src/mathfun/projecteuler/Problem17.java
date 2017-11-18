package mathfun.projecteuler;

import java.text.DecimalFormat;

public class Problem17
{
    private static final String[] NAMES_TENS = {"", "ten","twenty","thirty","forty","fifty","sixty","seventy","eighty",
    "ninety"};
    private static final String[] NAMES_NUMS = {"","one","two","three","four","five","six","seven","eight","nine","ten",
    "eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen"};

    public static void main(String[] args)
    {
        int sum = 0;
        for(int i = 1; i < 1000; i++)
            sum += translate(i).length();
        System.out.println(sum);
        System.out.println(sum + 11);
    }

    private static String translate(int num)
    {
        String s = "";
        String snum;
        String pad = "000000000000";
        DecimalFormat df = new DecimalFormat(pad);
        snum = df.format((long)num);
        int bil = Integer.parseInt(snum.substring(0,3));
        int mil = Integer.parseInt(snum.substring(3,6));
        int hunthou = Integer.parseInt(snum.substring(6,9));
        int thou = Integer.parseInt(snum.substring(9,12));
        String bils;
        switch(bil)
        {
            case 0:
                bils = "";
                break;
            case 1:
                bils = translate2(bil) + "billion";
                break;
            default:
                bils = translate2(bil) + "billion";
        }
        s += bils;
        String mils;
        switch(mil)
        {
            case 0:
                mils = "";
                break;
            default:
                mils = translate2(mil) + "million";
        }
        s += bils.equals("") ? "" : "and" + mils;
        String hunthous;
        switch(hunthou)
        {
            case 0:
                hunthous = "";
                break;
            case 1:
                hunthous = "onethousand";
                break;
            default:
                hunthous = translate2(hunthou);
        }
        s += mils.equals("") ? "" : "and" + hunthous;
        String thous;
        thous = translate2(thou);
        s += hunthous.equals("") ? thous : thous.equals("") ? hunthous : "and" + thous;
        return s;
    }

    private static String translate2(int num)
    {
        String s = "";
        if(num % 100 < 20)
        {
            s = NAMES_NUMS[num % 100];
            num /= 100;
        }
        else
        {
            s = NAMES_NUMS[num % 10];
            num /= 10;
            s = NAMES_TENS[num % 10] + s;
            num /= 10;
        }
        if(num == 0)
            return s;
        return NAMES_NUMS[num] + "hundredand" + s;
    }
}
