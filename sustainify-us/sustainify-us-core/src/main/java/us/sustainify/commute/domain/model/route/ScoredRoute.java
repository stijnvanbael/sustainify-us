package us.sustainify.commute.domain.model.route;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import be.appify.framework.domain.AbstractEntity;

@Entity
@Table(name = "scored_route")
public class ScoredRoute extends AbstractEntity {
	private static final long serialVersionUID = -1538496817136884111L;
	@Column(nullable = false)
	private int score;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
	@JoinColumn(name = "route_id")
	private Route route;

	@ManyToOne(optional = false, targetEntity = SustainifyUser.class)
	@JoinColumn(name = "user_id")
	private SustainifyUser user;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@Column(nullable = false)
	private LocalDate day;

	private ScoredRoute() {
		this.day = new LocalDate();
	}

	public ScoredRoute(Route route, int score, SustainifyUser user) {
		this();
		this.score = score;
		this.route = route;
		this.user = user;
	}

	public Route getRoute() {
		return route;
	}

	public int getScore() {
		return score;
	}

	public SustainifyUser getUser() {
		return user;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setUser(SustainifyUser user) {
		this.user = user;
	}
}
