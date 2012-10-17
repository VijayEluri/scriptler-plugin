/*
 * The MIT License
 *
 * Copyright (c) 2010, Dominik Bartholdi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.scriptler.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author imod
 * 
 */
public class ScriptSet {

    // have it sorted
    protected Set<Script> scriptSet = new TreeSet<Script>();

    public Script getScriptById(String id) {
        for (Script scr : scriptSet) {
            if (scr.getId().equals(id)) {
                return scr;
            }
        }
        return null;
    }

    public void removeScript(String id) {
        Script s = getScriptById(id);
        scriptSet.remove(s);
    }

    public void addOrReplace(Script script) {
        if (script != null) {
            Script oldScript = this.getScriptById(script.getId());
            if (oldScript != null) {
                Script mergedScript = merge(oldScript, script);
                scriptSet.remove(script);
                scriptSet.add(mergedScript);
            } else {
                scriptSet.add(script);
            }
        }
    }

    private Script merge(Script origin, Script newScript) {
        String name = StringUtils.isEmpty(newScript.name) ? origin.name : newScript.name;
        String comment = StringUtils.isEmpty(newScript.comment) ? origin.comment : newScript.comment;
        String originCatalog = StringUtils.isEmpty(newScript.originCatalog) ? origin.originCatalog : newScript.originCatalog;
        String originScript = StringUtils.isEmpty(newScript.originScript) ? origin.originScript : newScript.originScript;
        String originDate = StringUtils.isEmpty(newScript.originDate) ? origin.originDate : newScript.originDate;
        return new Script(newScript.getId(), name, comment, newScript.available, originCatalog, originScript, originDate, newScript.nonAdministerUsing, newScript.getParameters(), newScript.onlyMaster, newScript.getRepodir());
    }

    public final Set<Script> getScripts() {
        return Collections.unmodifiableSet(scriptSet);
    }

    public final Collection<Script> getScriptsForRepodir(final String repodir) {

        final Collection<Script> repoScripts = Collections2.filter(scriptSet, new Predicate<Script>() {
            public boolean apply(Script script) {
                return script.getRepodir().equals(repodir);
            }
        });

        return repoScripts;
    }

    public final Set<Script> getUserScripts() {
        Set<Script> userScripts = new TreeSet<Script>();
        for (Script script : scriptSet) {
            if (script.nonAdministerUsing) {
                userScripts.add(script);
            }
        }
        return userScripts;
    }

    public void setScripts(Set<Script> scripts) {
        this.scriptSet = scripts;
    }

}
