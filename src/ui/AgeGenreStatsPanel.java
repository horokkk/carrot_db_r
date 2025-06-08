package ui;

import dao.StatsDAO;
import dto.AgeGenreStat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AgeGenreStatsPanel extends JPanel {
    public AgeGenreStatsPanel() {
        setLayout(new BorderLayout());
        String[] cols = {"연령대", "장르", "리뷰수"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 데이터 로드
        List<AgeGenreStat> list = new StatsDAO().getAgeGenreStats();
        for (AgeGenreStat ag : list) {
            model.addRow(new Object[]{
                ag.getAgeGroup(), ag.getGenreName(), ag.getCount()
            });
        }
    }
}
