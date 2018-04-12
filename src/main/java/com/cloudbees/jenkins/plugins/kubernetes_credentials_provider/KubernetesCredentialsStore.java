package com.cloudbees.jenkins.plugins.kubernetes_credentials_provider;

import java.util.List;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.acegisecurity.Authentication;
import org.apache.commons.lang.NotImplementedException;
import org.jenkins.ui.icon.Icon;
import org.jenkins.ui.icon.IconSet;
import org.jenkins.ui.icon.IconType;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.ModelObject;
import hudson.security.ACL;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.CredentialsStoreAction;
import com.cloudbees.plugins.credentials.domains.Domain;

public class KubernetesCredentialsStore extends CredentialsStore {

    private final KubernetesCredentialProvider provider;
    private final KubernetesCredentialsStoreAction action = new KubernetesCredentialsStoreAction(this);

    public KubernetesCredentialsStore(KubernetesCredentialProvider provider) {
        super(KubernetesCredentialProvider.class);
        this.provider = provider;
    }

    @NonNull
    @Override
    public ModelObject getContext() {
        return Jenkins.getInstance();
    }

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Permission permission) {
        return Jenkins.getInstance().getACL().hasPermission(authentication, permission);
    }

    @NonNull
    @Override
    public List<Credentials> getCredentials(@NonNull Domain domain) {
        // TODO: Filter by domain - how do I do this?
        return  provider.getCredentials(Credentials.class, Jenkins.getInstance(), ACL.SYSTEM);
    }

    @Override
    public boolean addCredentials(@NonNull Domain domain, @NonNull Credentials credentials) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeCredentials(@NonNull Domain domain, @NonNull Credentials credentials) {
        throw new NotImplementedException();
    }

    @Override
    public boolean updateCredentials(@NonNull Domain domain, @NonNull Credentials current,
                                     @NonNull Credentials replacement) {
        throw new NotImplementedException();
    }

    @Nullable
    @Override
    public CredentialsStoreAction getStoreAction() {
        return action;
    }

    /**
     * Expose the store.
     */
    @ExportedBean
    public static class KubernetesCredentialsStoreAction extends CredentialsStoreAction {

        private final KubernetesCredentialsStore store;

        private KubernetesCredentialsStoreAction(KubernetesCredentialsStore store) {
            this.store = store;
            addIcons();
        }

        private void addIcons() {
            IconSet.icons.addIcon(new Icon("icon-credentials-kubernetes-store icon-sm",
                "kubernetes-credentials-provider/images/16x16/kubernetes-store.png",
                Icon.ICON_SMALL_STYLE, IconType.PLUGIN));
            IconSet.icons.addIcon(new Icon("icon-credentials-kubernetes-store icon-md",
                "kubernetes-credentials-provider/images/24x24/kubernetes-store.png",
                Icon.ICON_MEDIUM_STYLE, IconType.PLUGIN));
            IconSet.icons.addIcon(new Icon("icon-credentials-kubernetes-store icon-lg",
                "kubernetes-credentials-provider/images/32x32/kubernetes-store.png",
                Icon.ICON_LARGE_STYLE, IconType.PLUGIN));
            IconSet.icons.addIcon(new Icon("icon-credentials-kubernetes-store icon-xlg",
                "kubernetes-credentials-provider/images/48x48/kubernetes-store.png",
                Icon.ICON_XLARGE_STYLE, IconType.PLUGIN));
        }

        /**
         * {@inheritDoc}
         */
        @NonNull
        public CredentialsStore getStore() {
            return store;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getIconFileName() {
            return isVisible()
               ? "/plugin/kubernetes-credentials-provider/images/32x32/kubernetes-store.png"
               : null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getIconClassName() {
            return isVisible()
               ? "icon-credentials-kubernetes-store"
               : null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return "Kubernetes";
        }
    }
}
