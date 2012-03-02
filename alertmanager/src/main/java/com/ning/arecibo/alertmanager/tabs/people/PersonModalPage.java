/*
 * Copyright 2010-2012 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.arecibo.alertmanager.tabs.people;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import com.ning.arecibo.util.Logger;

import static com.ning.arecibo.alertmanager.utils.ModalWindowUtils.*;
import static com.ning.arecibo.alertmanager.utils.ModalWindowUtils.ModalMode.*;

public class PersonModalPage extends WebPage {
    final static Logger log = Logger.getLogger(PersonModalPage.class);

    public PersonModalPage(final ModalWindow window,final PeoplePanel parentPanel) {
        // insert by default
        this(window,parentPanel,new PersonFormModel(),INSERT);
    }

    public PersonModalPage(final ModalWindow window,final PeoplePanel parentPanel,PersonFormModel personFormModel,ModalMode mode) {

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        if(mode.equals(INSERT) || mode.equals(UPDATE)) {
            PersonInputFormPanel panel = new PersonInputFormPanel("formPanel",feedback,window,parentPanel,personFormModel,mode);
            panel.setOutputMarkupId(true);
            add(panel);
        }
        else if(mode.equals(DELETE)) {
            PersonDeleteFormPanel panel = new PersonDeleteFormPanel("formPanel",feedback,window,parentPanel,personFormModel,mode);
            panel.setOutputMarkupId(true);
            add(panel);
        }
        else {
            throw new IllegalStateException("Unrecognized mode: " + mode);
        }
    }
}
