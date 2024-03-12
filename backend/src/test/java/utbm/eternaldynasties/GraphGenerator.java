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

public class GraphGenerator {

    public GraphGenerator(ArrayList<Float> x, ArrayList<Long> y, String nom, String nomRessource){

        // Création du graphique
        JFreeChart chart = createChart(x, y, "Graphique de "+nomRessource, "Temps (s)", "Quantité");

        // Enregistrement du graphique en image
        saveChartAsImage(chart, nom);
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
            ChartUtilities.saveChartAsPNG(new File(fileName), chart, 800, 600);
            System.out.println("Graphique enregistré en tant qu'image : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}