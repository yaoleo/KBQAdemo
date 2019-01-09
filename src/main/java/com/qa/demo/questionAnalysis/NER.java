package com.qa.demo.questionAnalysis;

import com.qa.demo.dataStructure.Entity;
import com.qa.demo.dataStructure.Triplet;
import com.qa.demo.utils.kgprocess.KGTripletsClient;

import java.util.*;

/**
 *  Created time: 2017_09_08
 *  Author: Devin Hua
 *  Function description:
 *  The method to get entities in one question.
 */
public class NER {

    public static ArrayList<Entity> getEntities(String questionString) {
        HashMap<String, HashSet<String>> candidateEntities
                = new HashMap<>();
        ArrayList<Entity> eList = new ArrayList<>();
        ArrayList<Triplet> triplets = KGTripletsClient.getInstance().getKgTriplets();
        for (Triplet t : triplets) {
            String sname = t.getSubjectName();
            if (questionString.contains(sname)) {
                if (candidateEntities.isEmpty() ||
                        !candidateEntities.containsKey(sname)) {
                    HashSet<String> uris = new HashSet<>();
                    uris.add(t.getSubjectURI());
                    candidateEntities.put(sname, uris);
                } else if (candidateEntities.containsKey(sname)) {
                    HashSet<String> uris = candidateEntities.get(sname);
                    uris.add(t.getSubjectURI());
                    candidateEntities.put(sname, uris);
                }
            }
        }

        if(candidateEntities.isEmpty())
            return eList;

        if(candidateEntities.isEmpty())
            return eList;

        //若某个entity名包含于另一个entity名，则将其从候选中删掉；
        Set tempSet = candidateEntities.keySet();
        Iterator it1 = tempSet.iterator();
        ArrayList<String> keys = new ArrayList<String>();
        while (it1.hasNext()) {
            keys.add((String)it1.next());
        }

        for (String key : keys) {
            boolean deleteFlag = false; //若要将此key删掉，则置true;
            Iterator it2 = candidateEntities.keySet().iterator();
            while (it2.hasNext()) {
                String temp = (String)it2.next();
                if (temp.contains(key)&&!temp.equalsIgnoreCase(key)) {
                    deleteFlag = true;
                    break;
                }
            }
            if (deleteFlag)
                candidateEntities.remove(key);
        }

        if (!candidateEntities.isEmpty() || candidateEntities != null) {
            Iterator it = candidateEntities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, HashSet<String>> entry =
                        (Map.Entry<String, HashSet<String>>) it.next();
                for (String temp : entry.getValue()) {
                    Entity e = new Entity();
                    e.setEntityURI(temp);
                    e.setKgEntityName(entry.getKey());
                    eList.add(e);
                }
            }
        }
        return eList;
    }
}
