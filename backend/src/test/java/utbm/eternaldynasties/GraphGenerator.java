package utbm.eternaldynasties;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GraphGenerator {

    public GraphGenerator(ArrayList<Double> x, ArrayList<Double> y, String nom, String nomRessource) {
        JFreeChart chart = createChart(x, y, "Graphique de " + nomRessource);
        saveChartAsImage(chart, nom);
    }

    public GraphGenerator(Map<String, ArrayList<Double>> allData, ArrayList<Double> x, String nom) {
        JFreeChart chart = createChart(allData, x);
        saveChartAsImage(chart, nom);
    }

    private JFreeChart createChart(Map<String, ArrayList<Double>> allData, ArrayList<Double> x) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String nomRessource : allData.keySet()) {

            ArrayList<Double> y = allData.get(nomRessource);
            XYSeries series = new XYSeries(nomRessource);

            int diff = x.size() - y.size();

            for (int i = 0; i < x.size(); i++) {
                Double val = i < diff ? (double) 0 : y.get(i - diff);
                series.add(x.get(i), val);
            }


            dataset.addSeries(series);
        }

        return ChartFactory.createXYLineChart(
                "Graphique de toute les ressources",
                "Temps (s)",
                "Quantité",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createChart(ArrayList<Double> x, ArrayList<Double> y, String title) {
        XYSeries series = new XYSeries("Série");

        int diff = x.size() - y.size();

        for (int i = 0; i < x.size(); i++) {
            Double val = i < diff ? (double) 0 : y.get(i - diff);
            series.add(x.get(i), val);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return ChartFactory.createXYLineChart(
                title,
                "Temps (s)",
                "Quantité",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private void saveChartAsImage(JFreeChart chart, String fileName) {
        try {
            ChartUtilities.saveChartAsPNG(new File(fileName), chart, 1920, 1080);
            System.out.println("Graphique enregistré en tant qu'image : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}