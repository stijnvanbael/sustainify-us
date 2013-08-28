package us.sustainify.common.domain.model.organisation;

import be.appify.framework.domain.AbstractEntity;
import be.appify.framework.security.domain.User;
import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

import javax.persistence.*;

@Entity
@Table(name = "office_day")
public class OfficeDay extends AbstractEntity {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTimeAsTimestamp")
	private LocalTime arrival;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTimeAsTimestamp")
	private LocalTime departure;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    protected OfficeDay() {
    }

    public OfficeDay(User user, DayOfWeek dayOfWeek, LocalTime arrival, LocalTime departure) {
        this.user = user;
        this.dayOfWeek = dayOfWeek;
        this.arrival = arrival;
        this.departure = departure;
    }

	public LocalTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalTime arrival) {
		this.arrival = arrival;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
