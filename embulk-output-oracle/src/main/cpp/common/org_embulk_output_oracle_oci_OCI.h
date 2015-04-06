/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_embulk_output_oracle_oci_OCI */

#ifndef _Included_org_embulk_output_oracle_oci_OCI
#define _Included_org_embulk_output_oracle_oci_OCI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    createContext
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_embulk_output_oracle_oci_OCI_createContext
  (JNIEnv *, jobject);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    getLasetMessage
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_embulk_output_oracle_oci_OCI_getLasetMessage
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    open
 * Signature: ([BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_embulk_output_oracle_oci_OCI_open
  (JNIEnv *, jobject, jbyteArray, jstring, jstring, jstring);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    prepareLoad
 * Signature: ([BLorg/embulk/output/oracle/oci/TableDefinition;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_embulk_output_oracle_oci_OCI_prepareLoad
  (JNIEnv *, jobject, jbyteArray, jobject);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    loadBuffer
 * Signature: ([B[BI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_embulk_output_oracle_oci_OCI_loadBuffer
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jint);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    commit
 * Signature: ([B)Z
 */
JNIEXPORT jboolean JNICALL Java_org_embulk_output_oracle_oci_OCI_commit
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    rollback
 * Signature: ([B)Z
 */
JNIEXPORT jboolean JNICALL Java_org_embulk_output_oracle_oci_OCI_rollback
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     org_embulk_output_oracle_oci_OCI
 * Method:    close
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_org_embulk_output_oracle_oci_OCI_close
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
