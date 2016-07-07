==================================================================
 Presto Server Installation for an AWS EMR (Presto Admin and RPMs)
==================================================================

You can use Presto Admin and RPMs to install and deploy Presto on 
an AWS EMR.

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

Manual Installation for an AWs EMR
**********************************

Install and manage Presto manually on an AWS EMR in the Amazon cloud.

..
.. The following is from presto-admin/docs/emr.rst. Matt: If this looks good here, 
.. the text would be suitable for a "snippet."
..

To install, configure and run Presto Admin on an Amazon EMR cluster, follow the 
procedures in :ref:`quick-start-guide-label`, but pay attention to the notes or 
sections specfic to EMR cluster. We reiterate these EMR specific caveats below:

* To install Presto Admin on an Amazon EMR cluster, follow the instructions in 
  :ref:`presto-admin-installation-label` except for the following difference:

  - Use the online installer instead of the offline installer (see 
    :ref:`advanced-installation-options-label`).

* To configure Presto Admin on an Amazon EMR cluster, follow the instructions in 
  :ref:`presto-admin-configuration-label`. Specifically, we recommend the following 
  property values during the configuration: 

  - Use ``hadoop`` as the ``username`` instead of the default username ``root`` in 
    the ``config.json`` file.

  - Use the host name of the EMR master node as the ``coordinator`` in 
    the ``config.json`` file.

* To run Presto Admin on EMR, see the sections starting from 
  :ref:`presto-server-installation-label` onwards in :ref:`quick-start-guide-label`), 
  except for the following caveats:

  - The default version of Java installed on an EMR cluster (up to EMR 4.4.0) is 1.7, 
    whereas Presto requires Java 1.8. Install Java 1.8 on the EMR cluster by following 
    the instructions in :ref:`java-installation-label`.

  - For running Presto Admin commands on an EMR cluster, do the following:
    * Copy the ``.pem`` file associated with the Amazon EC2 key pair to the Presto Admin 
      installation node of the EMR cluster.
    * Use the ``-i <path to .pem file>`` input argument when running presto-admin 
      commands on the node.
		  ::

		   sudo </path/to/presto-admin> -i </path/to/your.pem> <presto_admin_command>

  - If you are using Presto Admin to install Presto server, make sure you download and use 
    the Presto Server RPM built for RPM Package Manager version > 4.6. Such an RPM should be 
    one of the artifacts you downloaded.

| :doc:`Installation Procedures <installation/installation-presto-admin>`
| Download **presto_server_pkg.141t.tar.gz** from :download:`teradata-presto`


.. toctree::
    :hidden:
  
    installation/installation-ambari
    installation/installation-yarn
    installation/installation-presto-admin


