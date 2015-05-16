package com.hywebchina.codebook.util;

import com.hywebchina.codebook.db.model.CodePage;

import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;

/**
 * Created by Grace on 2015/5/10.
 */
public class CodePageComparator implements Comparator {

    RuleBasedCollator collator;

    public CodePageComparator(){
        collator = (RuleBasedCollator) Collator.getInstance(java.util.Locale.CHINA);
    }
    public int compare(Object obj1, Object obj2) {
        String tempname1 = ((CodePage) obj1).getName();
        String tempname2 = ((CodePage) obj2).getName();

        CollationKey c1 = collator.getCollationKey(tempname1);
        CollationKey c2 = collator.getCollationKey(tempname2);

        return collator.compare(((CollationKey) c1).getSourceString(),((CollationKey) c2).getSourceString());
    }
}
