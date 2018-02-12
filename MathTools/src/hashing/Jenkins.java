package hashing;

import java.util.Arrays;

public class Jenkins
{
    public void mix(int a, int b, int c)
    {
        a -= b; a -= c; a ^= (c >> 13);
        b -= c; b -= a; b ^= (a << 8);
        c -= a; c -= b; c ^= (b >> 13);
        a -= b; a -= c; a ^= (c >> 12);
        b -= c; b -= a; b ^= (a << 16);
        c -= a; c -= b; c ^= (b >> 5);
        a -= b; a -= c; a ^= (c >> 3);
        b -= c; b -= a; b ^= (a << 10);
        c -= a; c -= b; c ^= (b >> 15);
    }

    public int hash(char[] k, int length, int init)
    {
        int a,b;
        int c = init;
        int len = length;
        a = b = 0x9e3779b9;
        int i = 0;
        while(len >= 12)
        {
            a += (k[i] + (k[i+1] << 8) + (k[i+2] << 16) + (k[i+3] << 24));
            b += (k[i+4] + (k[i+5] << 8) + (k[i+6] << 16) + (k[i+7] << 24));
            c += (k[i+8] + (k[i+9] << 8) + (k[i+10] << 16) + (k[i+11] << 24));
            mix(a,b,c);
            i += 12;
            len -= 12;
        }

        c += length;
        switch(len)
        {
            case 11: c += (k[10] << 24);
            case 10: c += (k[9] << 16);
            case 9: c += (k[8] << 8);
            case 8: b += (k[7] << 24);
            case 7: b += (k[6] << 16);
            case 6: b += (k[5] << 8);
            case 5: b += k[4];
            case 4: a += (k[3] << 24);
            case 3: a += (k[2] << 16);
            case 2: a += (k[1] << 8);
            case 1: a += k[0];
            default: break;
        }
        mix(a,b,c);
        return c & Integer.MAX_VALUE;
    }

    public static void main(String[] args)
    {
        String[] students = new String[5000];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student"+i;
        int[] map = new int[5000];
        Jenkins J = new Jenkins();
        for(String s : students)
        {
            int idx = J.hash(s.toCharArray(),s.length(),0xDEADBEEF) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
    }
}
