package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.impl.BasicSession;
import java.util.List;

import javax.ejb.Stateless;


import com.bizcreator.core.entity.Sequence;
import com.bizcreator.core.session.SequenceManager;

@Stateless
public class SeamSequenceSession extends BasicSession implements SequenceManager {

    //@DataModel
    private List<Sequence> sequences;
    //@DataModelSelection
    private Sequence selectSequence;
    //@In(create=true)
    //@Out(required=false)
    private Sequence _sequence;
    //@RequestParameter
    private String _forAction;

    //@Begin
    public String selectSequence() {
        _sequence = (Sequence) merge(selectSequence);
        System.out.println(">>>select sequence: " + _sequence);
        if ("delete".equals(_forAction)) {
            return "delete";
        } else {
            return "edit";
        }
    }

    //@End
    public String saveSequence() {
        System.out.println(">>>saving sequence: " + _sequence);
        if (_sequence != null) {
            System.out.println(">>>saving sequence id: " + _sequence.getId());
            if (_sequence.getId() != null) {
                merge(_sequence);
            } else {
                persist(_sequence);
            }
        }
        return "sequence";
    }

    //@End
    public String removeSequence() {
        if (selectSequence != null) {
            Sequence removing = (Sequence) find(Sequence.class, selectSequence.getId());
            if (removing != null) {
                remove(removing);
            }
        }
        return "sequence";
    }

    //@End
    public String cancel() {
        return "cancel";
    }

    //@WebRemote
    public String saveSequences(Sequence[] sequences) {
        String result = "";
        if (sequences != null) {
            for (Sequence sequence : sequences) {
                if (sequence.getId() != null) {
                    merge(sequence);
                } else {
                    persist(sequence);
                }
                result = result + sequence.getId() + ",";
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println(">>>saveSequences result: " + result);
        return result;
    }

    //@WebRemote
    public void removeSequences(String[] ids) {
        super.remove(Sequence.class, ids);
    }

    public String createSequence(String domain, String name, int nextValue, String description) {
        Sequence sequence = new Sequence(domain, name, nextValue, description);
        persist(sequence);
        return sequence.getId();
    }

    //@Factory("sequences")
    //@WebRemote
    public List<Sequence> findAllSequences() {
        sequences = executeQuery("from Sequence o ");
        return sequences;
    }

    //@WebRemote
    public List<Sequence> findSequences(int pageNum, int pageSize, String sortCol, String sortDir) {
        int pos = (pageNum - 1) * pageSize;
        List list = executeQuery("from Sequence o ");
        //.setMaxResults(pageSize)
        //.setFirstResult(pos)
        //.getResultList();
        return list;
    }

    public List findByDomain(String domain) {
        return executeQuery("from Sequence s where s.domain = ?", new Object[]{domain});
    }

    public void destroy() {
    }

    public Sequence findByName(String domain, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
