==================================================
Presto Server Installation for a Cluster (Ambari)
==================================================

You can use Ambari to install and deploy Presto on a 
cluster or YARN-based cluster.

System Requirements
*******************

* Hardware requirements:
 
  + Minimum 16GB RAM per node allocated for Presto

* Software requirements:

  + Ambari 2.1 (on Java 8)
  + Oracle Java JDK 1.8 (64-bit)
  + Red Hat Enterprise Linux 6.x (64-bit) or CentOS equivalent
  + Cluster: HDP 2.x
  + YARN-based cluster: HDP 2.2, which requires Hadoop 2.6
  + Python 2.6 or 2.7

----

Automated Installation for a Cluster
************************************

Install and manage Presto using `Apache Ambari`_ 2.1.

  .. _Apache Ambari: https://ambari.apache.org/

| :doc:`Installation Procedures <installation/installation-ambari>`
| Download **presto_ambari_pkg.141t.tar.gz** from :download:`teradata-presto`

----

Automated Installation for a YARN-Based Cluster
***********************************************

Install and manage Presto integrated with YARN using 
the `Apache Slider`_ view for `Apache Ambari`_ 2.1.

| :doc:`Installation Procedures <installation/installation-yarn-automated>`
| Download the YARN Presto package included in **presto_server_pkg.141t.tar.gz** from :download:`teradata-presto`

  .. _Apache Slider: https://slider.incubator.apache.org/
  .. _Apache Ambari: https://ambari.apache.org/

.. toctree::
    :hidden:
  
    installation/installation-ambari
    installation/installation-yarn

