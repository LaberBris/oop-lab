package edu.hitsz.rankingList;

import java.util.List;

public interface RankinglistDao {
    List<Rankinglist> getAllRanks();

    void doAdd(Rankinglist rankinglist);
    void doDelete(List<Rankinglist> rankinglists, String time);

}
