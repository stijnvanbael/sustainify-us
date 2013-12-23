package us.sustainify.web.authenticated;

import com.google.common.collect.Lists;
import us.sustainify.commute.domain.model.statistics.StatisticData;
import us.sustainify.commute.domain.model.statistics.StatisticDataSet;

import java.util.List;

public class GraphViewModel {
    private List<GraphPointModel> points;

    public GraphViewModel(StatisticDataSet<?> dataSet) {
        points = Lists.newArrayList();
        for(StatisticData<?> data : dataSet.getData()) {
            points.add(new GraphPointModel(data));
        }
    }

    public List<GraphPointModel> getPoints() {
        return points;
    }
}