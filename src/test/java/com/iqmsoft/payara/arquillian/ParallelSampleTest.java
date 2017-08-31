package com.iqmsoft.payara.arquillian;

import javax.ejb.EJB;

import org.junit.runner.RunWith;

import com.iqmsoft.payara.arquillian.ParallelSample;

import org.junit.Test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

@RunWith(Arquillian.class)
public class ParallelSampleTest {
    @EJB
    private ParallelSample sut;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(ParallelSample.class);
    }

    @Test()
    public void testExecute() {
        sut.execute();
    }
}
