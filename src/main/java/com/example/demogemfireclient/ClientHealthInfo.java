package com.example.demogemfireclient;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Entity
@Data @NoArgsConstructor
public class ClientHealthInfo implements Serializable{

    @Id @javax.persistence.Id
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
    public ClientHealthInfo(String accountId, Long steps, Long caloriesBurnt, Integer weight, Long energyBurnt, Long energyBurntGoal, Long exerciseTime, Long exerciseTimeGoal, Long standHours, Long standHoursGoal) {
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
