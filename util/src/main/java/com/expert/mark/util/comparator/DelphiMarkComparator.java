package com.expert.mark.util.comparator;

import com.expert.mark.model.method.data.DelphiMethodData;

import java.util.Comparator;

public class DelphiMarkComparator implements Comparator<DelphiMethodData.DelphiMark> {
    public int compare(DelphiMethodData.DelphiMark delphiMark, DelphiMethodData.DelphiMark t1) {
        return Integer.compare(delphiMark.getMark(), t1.getMark());
    }
}
