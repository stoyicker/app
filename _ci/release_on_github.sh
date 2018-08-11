#!/bin/bash
set -e

uploadReleaseToGitHub() {
    git fetch --tags
    LAST_TAG=$(git describe --tags --abbrev=0)
    THIS_RELEASE=$(git rev-parse --short HEAD)
    local IFS=$'\n'
    RELEASE_NOTES_ARRAY=($(git log --format=%B ${LAST_TAG}..${THIS_RELEASE} | tr -d '\r'))
    { for i in "${RELEASE_NOTES_ARRAY[@]}"
    do
        RELEASE_NOTES="$RELEASE_NOTES\\n$i"
    done
    }

    BODY="{
        \"tag_name\": \"$ARTIFACT_VERSION\",
        \"target_commitish\": \"master\",
        \"name\": \"$ARTIFACT_VERSION\",
        \"body\": \"$ARTIFACT_VERSION\"
    }"

    # Create the release in GitHub and extract its id from the response
    RESPONSE_BODY=$(curl -s \
            -u ${REPO_USER}:${GITHUB_TOKEN} \
            --header "Accept: application/vnd.github.v3+json" \
            --header "Content-Type: application/json; charset=utf-8" \
            --request POST \
            --data "${BODY}" \
            https://api.github.com/repos/"${TRAVIS_REPO_SLUG}"/releases)

    # Extract the upload_url value
    UPLOAD_URL=$(echo ${RESPONSE_BODY} | jq -r .upload_url)
    # And the id for later use
    RELEASE_ID=$(echo ${RESPONSE_BODY} | jq -r .id)

    cp app/build/outputs/apk/dev/debug/app.apk .

    # Attach the artifact
    UPLOAD_URL=$(echo ${UPLOAD_URL} | sed "s/{?name,label}/?name=app-dev-debug.apk/")
    RESPONSE_BODY=$(curl -s \
            -u ${REPO_USER}:${GITHUB_TOKEN} \
            --header "Accept: application/vnd.github.v3+json" \
            --header "Content-Type: application/zip" \
            --data-binary "@app-release.apk" \
            --request POST \
            ${UPLOAD_URL})

    echo "Release complete."
}

case ${TRAVIS_BRANCH} in
    *)
        uploadReleaseToGitHub
        ;;
    *)
        echo "Branch is ${TRAVIS_BRANCH}, which is not releasable. Skipping release."
        exit 0
        ;;
esac
