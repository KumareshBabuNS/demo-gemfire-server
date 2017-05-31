package com.example.demogemfireclient;

import com.gemstone.gemfire.pdx.PdxReader;
import com.gemstone.gemfire.pdx.PdxSerializable;
import com.gemstone.gemfire.pdx.PdxWriter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Entity
@Data @NoArgsConstructor
public class ClientHealthInfo
        implements PdxSerializable{

    @Override
    public void toData(PdxWriter pdxWriter) {
        pdxWriter.writeString("accountId", accountId)
                .markIdentityField("accountId");
        pdxWriter.writeLong("steps", steps);
        pdxWriter.writeLong("caloriesBurnt", caloriesBurnt);
        pdxWriter.writeInt("weight", weight);
        pdxWriter.writeLong("energyBurnt", energyBurnt);
        pdxWriter.writeLong("energyBurntGoal", energyBurntGoal);
        pdxWriter.writeLong("exerciseTime", exerciseTime);
        pdxWriter.writeLong("exerciseTimeGoal", exerciseTimeGoal);
        pdxWriter.writeLong("standHours", standHours);
        pdxWriter.writeLong("standHoursGoal", standHoursGoal);
    }

    @Override
    public void fromData(PdxReader pdxReader) {
        accountId=pdxReader.readString("accountId");
        steps=pdxReader.readLong("steps");
        caloriesBurnt=pdxReader.readLong("caloriesBurnt");
        weight=pdxReader.readInt("weight");
        energyBurnt=pdxReader.readLong("energyBurnt");
        energyBurntGoal=pdxReader.readLong("energyBurntGoal");
        exerciseTime=pdxReader.readLong("exerciseTime");
        exerciseTimeGoal=pdxReader.readLong("exerciseTimeGoal");
        standHours=pdxReader.readLong("standHours");
        standHoursGoal=pdxReader.readLong("standHoursGoal");
    }


    @Id
    @javax.persistence.Id
    private String accountId;
    private Long steps;
    private Long caloriesBurnt;
    private Integer weight;
    private Long energyBurnt;
    private Long energyBurntGoal;
    private Long exerciseTime;
    private Long exerciseTimeGoal;
    private Long standHours;
    private Long standHoursGoal;

    @PersistenceConstructor
    public ClientHealthInfo(
            String accountId, Long steps, Long caloriesBurnt, Integer weight, Long energyBurnt, Long energyBurntGoal, Long exerciseTime, Long exerciseTimeGoal, Long standHours, Long standHoursGoal) {
        this.accountId = accountId;
        this.steps = steps;
        this.caloriesBurnt = caloriesBurnt;
        this.weight = weight;
        this.energyBurnt = energyBurnt;
        this.energyBurntGoal = energyBurntGoal;
        this.exerciseTime = exerciseTime;
        this.exerciseTimeGoal = exerciseTimeGoal;
        this.standHours = standHours;
        this.standHoursGoal = standHoursGoal;
    }
}
