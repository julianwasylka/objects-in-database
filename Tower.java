package org.example;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
public class Tower {
    @Id @Getter
    private String name;
    private int height;

    @OneToMany @Getter @JoinColumn(name="mage")
    private List<Mage> mages;

    public Tower(String name, int height, List<Tower> t) {
        this.name = name;
        this.height = height;
        mages = new ArrayList<Mage>();
        t.add(this);
    }

    @Override
    public String toString() {
        return "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", mages=" + mages.toString() +
                '}';
    }

    public void removeMages(EntityManager em) {
        for (Mage mage : mages) {
            mage.setTower(null);
            em.refresh(mage);
        }
        mages.clear();
    }
}
