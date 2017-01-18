package khrom.test.sanzone.model.dto.create;

import javax.validation.constraints.NotNull;

import static khrom.test.sanzone.model.error.ErrorCode.FIELD_MISSING;

/**
 * Created by DEV on 9/29/2016.
 */
public class CreateAntennaDTO {

    @NotNull( message = FIELD_MISSING )
    private Double power;

    @NotNull( message = FIELD_MISSING )
    private Double gain;

    @NotNull( message = FIELD_MISSING )
    private Double tractLost;

    @NotNull( message = FIELD_MISSING )
    private Double earthFactor;

    @NotNull( message = FIELD_MISSING )
    private Double diagramWidthHalfPowerH;

    @NotNull( message = FIELD_MISSING )
    private Double diagramWidthHalfPowerV;

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }

    public Double getTractLost() {
        return tractLost;
    }

    public void setTractLost(Double tractLost) {
        this.tractLost = tractLost;
    }

    public Double getEarthFactor() {
        return earthFactor;
    }

    public void setEarthFactor(Double earthFactor) {
        this.earthFactor = earthFactor;
    }

    public Double getDiagramWidthHalfPowerH() {
        return diagramWidthHalfPowerH;
    }

    public void setDiagramWidthHalfPowerH(Double diagramWidthHalfPowerH) {
        this.diagramWidthHalfPowerH = diagramWidthHalfPowerH;
    }

    public Double getDiagramWidthHalfPowerV() {
        return diagramWidthHalfPowerV;
    }

    public void setDiagramWidthHalfPowerV(Double diagramWidthHalfPowerV) {
        this.diagramWidthHalfPowerV = diagramWidthHalfPowerV;
    }

    public Double getP() {
        return power;
    }

    public Double getG() {
        return gain;
    }

    public Double getTL() {
        return tractLost;
    }

    public Double getEF() {
        return earthFactor;
    }

    public Double getQ05H() {
        return diagramWidthHalfPowerH;
    }

    public Double getQ05V() {
        return diagramWidthHalfPowerV;
    }
}