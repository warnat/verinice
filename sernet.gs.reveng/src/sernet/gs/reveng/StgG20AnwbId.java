package sernet.gs.reveng;

// Generated Jun 5, 2015 1:28:30 PM by Hibernate Tools 3.4.0.CR1

import java.sql.Clob;

/**
 * StgG20AnwbId generated by hbm2java
 */
public class StgG20AnwbId implements java.io.Serializable {

	private int snr;
	private Integer sysSnr;
	private Integer anwSnr;
	private String klasse;
	private Clob besanw;
	private Clob besfg;
	private Clob besvi;
	private Clob abh;
	private Clob abhdest;
	private Clob abhsrc;
	private String lock;
	private Integer bewvert;
	private Clob begrvert;
	private Integer bewintegr;
	private Clob begrintegr;
	private Integer bewverfu;
	private Clob begrverfu;
	private Integer expSnr;
	private Integer stat;
	private Integer benSnr;
	private Integer memoSnr;

	public StgG20AnwbId() {
	}

	public StgG20AnwbId(int snr) {
		this.snr = snr;
	}

	public StgG20AnwbId(int snr, Integer sysSnr, Integer anwSnr, String klasse,
			Clob besanw, Clob besfg, Clob besvi, Clob abh, Clob abhdest,
			Clob abhsrc, String lock, Integer bewvert, Clob begrvert,
			Integer bewintegr, Clob begrintegr, Integer bewverfu,
			Clob begrverfu, Integer expSnr, Integer stat, Integer benSnr,
			Integer memoSnr) {
		this.snr = snr;
		this.sysSnr = sysSnr;
		this.anwSnr = anwSnr;
		this.klasse = klasse;
		this.besanw = besanw;
		this.besfg = besfg;
		this.besvi = besvi;
		this.abh = abh;
		this.abhdest = abhdest;
		this.abhsrc = abhsrc;
		this.lock = lock;
		this.bewvert = bewvert;
		this.begrvert = begrvert;
		this.bewintegr = bewintegr;
		this.begrintegr = begrintegr;
		this.bewverfu = bewverfu;
		this.begrverfu = begrverfu;
		this.expSnr = expSnr;
		this.stat = stat;
		this.benSnr = benSnr;
		this.memoSnr = memoSnr;
	}

	public int getSnr() {
		return this.snr;
	}

	public void setSnr(int snr) {
		this.snr = snr;
	}

	public Integer getSysSnr() {
		return this.sysSnr;
	}

	public void setSysSnr(Integer sysSnr) {
		this.sysSnr = sysSnr;
	}

	public Integer getAnwSnr() {
		return this.anwSnr;
	}

	public void setAnwSnr(Integer anwSnr) {
		this.anwSnr = anwSnr;
	}

	public String getKlasse() {
		return this.klasse;
	}

	public void setKlasse(String klasse) {
		this.klasse = klasse;
	}

	public Clob getBesanw() {
		return this.besanw;
	}

	public void setBesanw(Clob besanw) {
		this.besanw = besanw;
	}

	public Clob getBesfg() {
		return this.besfg;
	}

	public void setBesfg(Clob besfg) {
		this.besfg = besfg;
	}

	public Clob getBesvi() {
		return this.besvi;
	}

	public void setBesvi(Clob besvi) {
		this.besvi = besvi;
	}

	public Clob getAbh() {
		return this.abh;
	}

	public void setAbh(Clob abh) {
		this.abh = abh;
	}

	public Clob getAbhdest() {
		return this.abhdest;
	}

	public void setAbhdest(Clob abhdest) {
		this.abhdest = abhdest;
	}

	public Clob getAbhsrc() {
		return this.abhsrc;
	}

	public void setAbhsrc(Clob abhsrc) {
		this.abhsrc = abhsrc;
	}

	public String getLock() {
		return this.lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public Integer getBewvert() {
		return this.bewvert;
	}

	public void setBewvert(Integer bewvert) {
		this.bewvert = bewvert;
	}

	public Clob getBegrvert() {
		return this.begrvert;
	}

	public void setBegrvert(Clob begrvert) {
		this.begrvert = begrvert;
	}

	public Integer getBewintegr() {
		return this.bewintegr;
	}

	public void setBewintegr(Integer bewintegr) {
		this.bewintegr = bewintegr;
	}

	public Clob getBegrintegr() {
		return this.begrintegr;
	}

	public void setBegrintegr(Clob begrintegr) {
		this.begrintegr = begrintegr;
	}

	public Integer getBewverfu() {
		return this.bewverfu;
	}

	public void setBewverfu(Integer bewverfu) {
		this.bewverfu = bewverfu;
	}

	public Clob getBegrverfu() {
		return this.begrverfu;
	}

	public void setBegrverfu(Clob begrverfu) {
		this.begrverfu = begrverfu;
	}

	public Integer getExpSnr() {
		return this.expSnr;
	}

	public void setExpSnr(Integer expSnr) {
		this.expSnr = expSnr;
	}

	public Integer getStat() {
		return this.stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Integer getBenSnr() {
		return this.benSnr;
	}

	public void setBenSnr(Integer benSnr) {
		this.benSnr = benSnr;
	}

	public Integer getMemoSnr() {
		return this.memoSnr;
	}

	public void setMemoSnr(Integer memoSnr) {
		this.memoSnr = memoSnr;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StgG20AnwbId))
			return false;
		StgG20AnwbId castOther = (StgG20AnwbId) other;

		return (this.getSnr() == castOther.getSnr())
				&& ((this.getSysSnr() == castOther.getSysSnr()) || (this
						.getSysSnr() != null && castOther.getSysSnr() != null && this
						.getSysSnr().equals(castOther.getSysSnr())))
				&& ((this.getAnwSnr() == castOther.getAnwSnr()) || (this
						.getAnwSnr() != null && castOther.getAnwSnr() != null && this
						.getAnwSnr().equals(castOther.getAnwSnr())))
				&& ((this.getKlasse() == castOther.getKlasse()) || (this
						.getKlasse() != null && castOther.getKlasse() != null && this
						.getKlasse().equals(castOther.getKlasse())))
				&& ((this.getBesanw() == castOther.getBesanw()) || (this
						.getBesanw() != null && castOther.getBesanw() != null && this
						.getBesanw().equals(castOther.getBesanw())))
				&& ((this.getBesfg() == castOther.getBesfg()) || (this
						.getBesfg() != null && castOther.getBesfg() != null && this
						.getBesfg().equals(castOther.getBesfg())))
				&& ((this.getBesvi() == castOther.getBesvi()) || (this
						.getBesvi() != null && castOther.getBesvi() != null && this
						.getBesvi().equals(castOther.getBesvi())))
				&& ((this.getAbh() == castOther.getAbh()) || (this.getAbh() != null
						&& castOther.getAbh() != null && this.getAbh().equals(
						castOther.getAbh())))
				&& ((this.getAbhdest() == castOther.getAbhdest()) || (this
						.getAbhdest() != null && castOther.getAbhdest() != null && this
						.getAbhdest().equals(castOther.getAbhdest())))
				&& ((this.getAbhsrc() == castOther.getAbhsrc()) || (this
						.getAbhsrc() != null && castOther.getAbhsrc() != null && this
						.getAbhsrc().equals(castOther.getAbhsrc())))
				&& ((this.getLock() == castOther.getLock()) || (this.getLock() != null
						&& castOther.getLock() != null && this.getLock()
						.equals(castOther.getLock())))
				&& ((this.getBewvert() == castOther.getBewvert()) || (this
						.getBewvert() != null && castOther.getBewvert() != null && this
						.getBewvert().equals(castOther.getBewvert())))
				&& ((this.getBegrvert() == castOther.getBegrvert()) || (this
						.getBegrvert() != null
						&& castOther.getBegrvert() != null && this
						.getBegrvert().equals(castOther.getBegrvert())))
				&& ((this.getBewintegr() == castOther.getBewintegr()) || (this
						.getBewintegr() != null
						&& castOther.getBewintegr() != null && this
						.getBewintegr().equals(castOther.getBewintegr())))
				&& ((this.getBegrintegr() == castOther.getBegrintegr()) || (this
						.getBegrintegr() != null
						&& castOther.getBegrintegr() != null && this
						.getBegrintegr().equals(castOther.getBegrintegr())))
				&& ((this.getBewverfu() == castOther.getBewverfu()) || (this
						.getBewverfu() != null
						&& castOther.getBewverfu() != null && this
						.getBewverfu().equals(castOther.getBewverfu())))
				&& ((this.getBegrverfu() == castOther.getBegrverfu()) || (this
						.getBegrverfu() != null
						&& castOther.getBegrverfu() != null && this
						.getBegrverfu().equals(castOther.getBegrverfu())))
				&& ((this.getExpSnr() == castOther.getExpSnr()) || (this
						.getExpSnr() != null && castOther.getExpSnr() != null && this
						.getExpSnr().equals(castOther.getExpSnr())))
				&& ((this.getStat() == castOther.getStat()) || (this.getStat() != null
						&& castOther.getStat() != null && this.getStat()
						.equals(castOther.getStat())))
				&& ((this.getBenSnr() == castOther.getBenSnr()) || (this
						.getBenSnr() != null && castOther.getBenSnr() != null && this
						.getBenSnr().equals(castOther.getBenSnr())))
				&& ((this.getMemoSnr() == castOther.getMemoSnr()) || (this
						.getMemoSnr() != null && castOther.getMemoSnr() != null && this
						.getMemoSnr().equals(castOther.getMemoSnr())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSnr();
		result = 37 * result
				+ (getSysSnr() == null ? 0 : this.getSysSnr().hashCode());
		result = 37 * result
				+ (getAnwSnr() == null ? 0 : this.getAnwSnr().hashCode());
		result = 37 * result
				+ (getKlasse() == null ? 0 : this.getKlasse().hashCode());
		result = 37 * result
				+ (getBesanw() == null ? 0 : this.getBesanw().hashCode());
		result = 37 * result
				+ (getBesfg() == null ? 0 : this.getBesfg().hashCode());
		result = 37 * result
				+ (getBesvi() == null ? 0 : this.getBesvi().hashCode());
		result = 37 * result
				+ (getAbh() == null ? 0 : this.getAbh().hashCode());
		result = 37 * result
				+ (getAbhdest() == null ? 0 : this.getAbhdest().hashCode());
		result = 37 * result
				+ (getAbhsrc() == null ? 0 : this.getAbhsrc().hashCode());
		result = 37 * result
				+ (getLock() == null ? 0 : this.getLock().hashCode());
		result = 37 * result
				+ (getBewvert() == null ? 0 : this.getBewvert().hashCode());
		result = 37 * result
				+ (getBegrvert() == null ? 0 : this.getBegrvert().hashCode());
		result = 37 * result
				+ (getBewintegr() == null ? 0 : this.getBewintegr().hashCode());
		result = 37
				* result
				+ (getBegrintegr() == null ? 0 : this.getBegrintegr()
						.hashCode());
		result = 37 * result
				+ (getBewverfu() == null ? 0 : this.getBewverfu().hashCode());
		result = 37 * result
				+ (getBegrverfu() == null ? 0 : this.getBegrverfu().hashCode());
		result = 37 * result
				+ (getExpSnr() == null ? 0 : this.getExpSnr().hashCode());
		result = 37 * result
				+ (getStat() == null ? 0 : this.getStat().hashCode());
		result = 37 * result
				+ (getBenSnr() == null ? 0 : this.getBenSnr().hashCode());
		result = 37 * result
				+ (getMemoSnr() == null ? 0 : this.getMemoSnr().hashCode());
		return result;
	}

}