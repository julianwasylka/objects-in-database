package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        List<Tower> towers = new ArrayList<>();
        List<Mage> mages = new ArrayList<>();
        Tower t1 = new Tower("t1", 90, towers);
        Tower t2 = new Tower("t2", 115, towers);
        Mage m1 = new Mage("m1", 7, t1, mages);
        Mage m2 = new Mage("m2", 5, t1, mages);
        Mage m3 = new Mage("m3", 14, t1, mages);
        Mage m4 = new Mage("m4", 2, t1, mages);
        Mage m5 = new Mage("m5", 26, t2, mages);
        Mage m6 = new Mage("m6", 18, t2, mages);
        Mage m7 = new Mage("m7", 12, t2, mages);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(t1);
        em.persist(t2);
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);
        em.persist(m5);
        em.persist(m6);
        em.persist(m7);
        tx.commit();

        boolean cont = true;
        Scanner scnr = new Scanner(System.in);
        while (cont) {
            System.out.println("\n1 - wszystko, 2 - dodaj, 3 - usun, 4 - mag z wiezy, 5 - wyjdz");
            int n = scnr.nextInt();
            tx.begin();
            switch (n) {
                case 1:
                    showAll(em);
                    break;
                case 2:
                    System.out.println("dodaj 1 - mag, 2 - wieza");
                    int k = scnr.nextInt();
                    String name = scnr.next();
                    if (k == 1) {
                        int lvl = scnr.nextInt();
                        String tow = scnr.next();
                        Tower t = null;
                        for (int i = 0; i < towers.size(); i++) {
                            if (towers.get(i).getName().equals(tow))
                                t = towers.get(i);
                        }
                        if (t != null) {
                            Mage m = new Mage(name, lvl, t, mages);
                            em.persist(m);
                        }
                        break;
                    }
                    else if (k == 2) {
                        int height = scnr.nextInt();
                        Tower t = new Tower(name, height, towers);
                        em.persist(t);
                        break;
                    }
                    else break;
                case 3:
                    System.out.println("usun 1 - mag, 2 - wieza");
                    int k2 = scnr.nextInt();
                    String name2 = scnr.next();
                    if (k2 == 1) {
                        Mage m = null;
                        for (int i = 0; i < mages.size(); i++) {
                            if (mages.get(i).getName().equals(name2))
                                m = mages.get(i);
                        }
                        if (m != null) {
                            em.remove(m);
                            mages.remove(m);
                            em.refresh(m.getTower());
                        }
                        break;
                    }
                    else if (k2 == 2) {
                        Tower t0 = null;
                        for (int i = 0; i < towers.size(); i++) {
                            if (towers.get(i).getName().equals(name2))
                               t0 = towers.get(i);
                        }
                        if (t0 != null) {
                            t0.removeMages(em);
                            em.remove(t0);
                            towers.remove(t0);
                        }
                        break;
                    }
                    else break;
                case 4:
                    int lvl = scnr.nextInt();
                    String tow = scnr.next();
                    Tower t = null;
                    for (int i = 0; i < towers.size(); i++) {
                        if (towers.get(i).getName().equals(tow))
                            t = towers.get(i);
                    }
                    if (t != null) {
                        magesAboveInTower(em, lvl, t);
                    }
                    break;
                case 5:
                    cont = false;
                    break;
            }
            tx.commit();
        }

        em.close();
        emf.close();
    }

    private static void showAll(EntityManager em) {
        String queryStrM = "SELECT m FROM Mage m";
        Query query1 = em.createQuery(queryStrM);
        List<Object> result = query1.getResultList();
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }

        String queryStrT = "SELECT t FROM Tower t";
        Query query2 = em.createQuery(queryStrT);
        List<Tower> towers = query2.getResultList();
        for (int i = 0; i < towers.size(); i++)
        {
            System.out.println(towers.get(i));
        }
    }

    private static void magesAbove(EntityManager em, int lvl) {
        String quertStr = "SELECT m FROM Mage m WHERE m.level > :lvl";
        Query query = em.createQuery(quertStr);
        query.setParameter("lvl", lvl);
        List<Mage> result = query.getResultList();

        System.out.println("\nmagesAbove:");
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }
    }

    private static void towersShorter(EntityManager em, int height) {
        String quertStr = "SELECT t FROM Tower t WHERE t.height < :height";
        Query query = em.createQuery(quertStr);
        query.setParameter("height", height);
        List<Tower> result = query.getResultList();

        System.out.println("\ntowersShorter:");
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }
    }

    private static void magesAboveInTower(EntityManager em, int lvl, Tower t) {
        String quertStr = "SELECT m FROM Mage m WHERE m.level > :lvl AND m.tower LIKE :t";
        Query query = em.createQuery(quertStr);
        query.setParameter("lvl", lvl);
        query.setParameter("t", t);
        List<Mage> result = query.getResultList();

        System.out.println("\nmagesAboveInTower:");
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }
    }
}