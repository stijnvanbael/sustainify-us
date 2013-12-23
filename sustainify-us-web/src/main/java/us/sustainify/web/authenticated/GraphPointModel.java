package us.sustainify.web.authenticated;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import us.sustainify.commute.domain.model.statistics.StatisticData;

import java.util.Date;

public class GraphPointModel {
    private final StatisticData<?> data;

    public GraphPointModel(StatisticData<?> data) {
        this.data = data;
    }

    public Date getStart() {
        return data.getInterval().getStart().toDate();
    }

    public Date getEnd() {
        return data.getInterval().getEnd().toDate();
    }

    public Double getData() {
        Object value = data.getValue();
        return (value instanceof Mass) ? ((Mass) value).getKilograms() :
                (value instanceof Length) ? ((Length) value).getKilometers() : Double.parseDouble(value.toString());
    }

}
