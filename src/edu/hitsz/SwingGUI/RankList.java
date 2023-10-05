package edu.hitsz.SwingGUI;

import edu.hitsz.application.Main;
import edu.hitsz.rankingList.Rankinglist;
import edu.hitsz.rankingList.RankinglistDaompl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class RankList {
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JLabel headerLabel;
    private JButton deleteButton;
    private JTable rankTable;
    private JScrollPane scrollPanel;



    public RankList(String gameDegree) {
        headerLabel.setText("难度：" + gameDegree);
        RankinglistDaompl rankinglistDaompl = new RankinglistDaompl();


        String[] columnName = {"排名", "用户名", "分数", "时间"};
        String[][] tableData = new String[30][4];

        File f = new File("Ranklist.txt");
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new FileReader(f));
            String str;
            for(int i=0; i<tableData.length; i++) {
                if ((str = buff.readLine() ) != null) {
                    String[] tempStr = str.split("\\$");
                    if ( !tempStr[3].equals(gameDegree) ) {
                        --i;
                        continue;
                    }
                    tableData[i][0] = String.valueOf(i+1);
                    tableData[i][1] = tempStr[0];
                    tableData[i][2] = tempStr[1];
                    tableData[i][3] = tempStr[2];
                }
            }
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnName){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        rankTable.setModel(model);
        scrollPanel.setViewportView(rankTable);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rankTable.getSelectedRow();
                System.out.println(row);
                int result = JOptionPane.showConfirmDialog(deleteButton,
                        "是否确定中删除？");
                if (JOptionPane.YES_OPTION == result && row != -1) {
                    String deleteUsername = rankTable.getValueAt(row, 1).toString();
                    String deleteScore = rankTable.getValueAt(row, 2).toString();
                    String deleteTime = rankTable.getValueAt(row, 3).toString();
                    model.removeRow(row);

                    System.out.println(deleteTime);

                    File oldf = new File("Ranklist.txt");
                    File newf = new File("Ranklist.tmp");
                    BufferedReader buff = null;
                    PrintWriter pw = null;

                    try {
                        buff = new BufferedReader(new FileReader(oldf));
                        pw = new PrintWriter(new FileWriter(newf));
                        String str;
                        while ((str = buff.readLine() ) != null) {
                            String[] tempStr = str.split("\\$");
                            if (tempStr[2].equals(deleteTime) && tempStr[0].equals(deleteUsername) && tempStr[1].equals(deleteScore)) {
                                pw.flush();
                            } else {
                                pw.println(str);
                                pw.flush();
                            }

                        }
                        pw.close();
                        buff.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    oldf.delete();
                    newf.renameTo(oldf);

                    RankList rankList = new RankList(gameDegree);
                    Main.cardPanel.add(rankList.getMainPanel());
                    Main.cardLayout.last(Main.cardPanel);

                }
            }
        });


    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
