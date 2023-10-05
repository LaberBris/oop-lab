package edu.hitsz.rankingList;

import edu.hitsz.application.Main;
import edu.hitsz.rankingList.RankinglistDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintRankingList{
    public void printRankList(int score, String username, boolean gameOverFlag, String gameDegree) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date();
        String time = simpleDateFormat.format(date);

        RankinglistDaompl rankinglistDaompl = new RankinglistDaompl();
        rankinglistDaompl.rFile();
        rankinglistDaompl.clrFile();
        if (gameOverFlag == true) {
            rankinglistDaompl.doAdd(new Rankinglist(username, score, time, gameDegree));
        }
        List<Rankinglist> rankinglists = rankinglistDaompl.getAllRanks();
        rankinglistDaompl.wFile(rankinglists);

        System.out.println("************************************************************");
        System.out.println("                        得 分 排 行 榜                        ");
        System.out.println("************************************************************");
        System.out.println("排名       用户名       分数       时间         难度");
        int i = 1;
        for(Rankinglist rankinglist: rankinglists) {
            System.out.printf("第%d名:   %10s%8d   %12s   %4s\n", i, rankinglist.getUsername(),
                   rankinglist.getScore(), rankinglist.getTime() ,rankinglist.getDegree());
            i++;
        }

    }
}
