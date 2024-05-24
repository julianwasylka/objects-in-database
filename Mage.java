package org.example;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
public class Mage {
    @Id @Getter
    private String name;

    @Setter @Getter
    private int level;

    @ManyToOne @Getter @JoinColumn(name="tower")
    private Tower tower;


    public void setTower(Tower tower) {
        this.tower.getMages().remove(this);
        if (tower != null) {
            this.tower = tower;
            tower.getMages().add(this);
        }
        else {
            this.tower = null;
        }
    }

    public Mage(String name, int level, Tower tower, List<Mage> m) {
        this.name = name;
        this.level = level;
        this.tower = tower;
        tower.getMages().add(this);
        m.add(this);
    }

    @Override
    public String toString() {
        String towerName = (tower != null) ? tower.getName() : "None";
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", tower=" + towerName +
                '}';
    }
}
