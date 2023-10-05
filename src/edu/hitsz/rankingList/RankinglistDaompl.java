package edu.hitsz.rankingList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankinglistDaompl implements RankinglistDao{
    private List<Rankinglist> rankinglists;

    File f = new File("Ranklist.txt");



    public void rFile() {
        rankinglists = new ArrayList<Rankinglist>();
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new FileReader(f));
            String str;
            while ( (str = buff.readLine() ) != null) {
                String[] tempStr = str.split("\\$");
                Rankinglist rankinglist = new Rankinglist(tempStr[0], Integer.parseInt(tempStr[1]), tempStr[2], tempStr[3]);
                rankinglists.add(rankinglist);
            }
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wFile(List<Rankinglist> rankinglists) {
        try {
            FileWriter fw = new FileWriter(f, true);
            for (Rankinglist rankinglist: rankinglists) {
                String str = rankinglist.getUsername() + "$" + rankinglist.getScore() + "$" + rankinglist.getTime() + "$" + rankinglist.getDegree();
                fw.write(str + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clrFile() {
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取所有图书
    @Override
    public List<Rankinglist> getAllRanks() {
        //调用Collections类下的方法
        Collections.sort(rankinglists, new Comparator<Rankinglist>() {
            @Override
            public int compare(Rankinglist o1, Rankinglist o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        return rankinglists;
    }

    //新增图书
    @Override
    public void doAdd(Rankinglist rankinglist) {
        rankinglists.add(rankinglist);
    }

    @Override
    public void doDelete(List<Rankinglist> rankinglists, String time) {
        for (Rankinglist rankinglist: rankinglists) {
            if (rankinglist.getTime().equals(time)) {
                rankinglists.remove(rankinglist);
            }
        }
    }

}
