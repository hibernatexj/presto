=================================================================
Presto Server Installation for a Cluster (Presto Admin and RPMs)
=================================================================

You can use Presto Admin and RPMs to install and deploy Presto on a 
cluster or YARN-based cluster.


System Requirements
*******************

* Hardware requirements:
 
  + Minimum 16GB RAM per node allocated for Presto

* Software requirements:

  + Red Hat Enterprise Linux 6.x (64-bit) or CentOS equivalent
  + Oracle Java JDK 1.8 (64-bit)  
  + Cluster: CDH 5.x or HDP 2.x
  + YARN-based cluster: CDH 5.4 or HDP 2.2; both require Hadoop 2.6
  + Python 2.6 or 2.7

Manual Installation for a Cluster
*********************************

Install and manage Presto manually using Presto Admin.

| :doc:`Installation Procedures <installation/installation-presto-admin>`
| Download **presto_server_pkg.141t.tar.gz** from :download:`teradata-presto`

----

Manual Installation for a YARN-Based Cluster
********************************************

Install and manage Presto integrated with YARN manually using `Apache Slider`_.

| :doc:`Installation Procedures <installation/installation-yarn-manual>`
| Download the YARN Presto package included in **presto_server_pkg.141t.tar.gz** from :download:`teradata-presto`
| Download :download:`apache-slider`

  .. _Apache Slider: https://slider.incubator.apache.org/

.. toctree::
    :hidden:
  
    installation/installation-ambari
    installation/installation-yarn
    installation/installation-presto-admin

----
