package utbm.eternaldynasties;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GraphGenerator {

    public GraphGenerator(ArrayList<Float> x, ArrayList<Long> y, String nom, String nomRessource){
        JFreeChart chart = createChart(x, y, "Graphique de "+nomRessource, "Temps (s)", "Quantité");
        saveChartAsImage(chart, nom);
    }

    public GraphGenerator(Map<String, ArrayList<Long>> allData, ArrayList<Float> x, String nom) {
        JFreeChart chart = createChart(allData, x,"Graphique de toute les ressources", "Temps (s)", "Quantité");
        saveChartAsImage(chart, nom);
    }

    private JFreeChart createChart(Map<String, ArrayList<Long>> allData, ArrayList<Float> x, String title, String xAxisLabel, String yAxisLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String nomRessource : allData.keySet()) {

            ArrayList<Long> y = allData.get(nomRessource);
            XYSeries series = new XYSeries(nomRessource);

            int diff = x.size() - y.size();

            for (int i = 0; i < x.size(); i++) {
                Double val = i < diff ? (double) 0 : y.get(i - diff);
                series.add(x.get(i), val);
            }


            dataset.addSeries(series);
        }

        return ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createChart(ArrayList<Float> x, ArrayList<Long> y, String title, String xAxisLabel, String yAxisLabel) {
        XYSeries series = new XYSeries("Série");

        int diff = x.size()-y.size();

        for (int i = 0; i < x.size(); i++) {
            Double val = i<diff?(double)0:y.get(i-diff);
            series.add(x.get(i),val );
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private void saveChartAsImage(JFreeChart chart, String fileName) {
        try {
            ChartUtilities.saveChartAsPNG(new File(fileName), chart, 1920,1080);
            System.out.println("Graphique enregistré en tant qu'image : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}