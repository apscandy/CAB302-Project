#!/bin/bash

# Exit on error
set -e

# Define source and destination paths
SOURCE_DIR="target/site/apidocs"
DEST_DIR="docs"

# Check if the source directory exists
if [ ! -d "$SOURCE_DIR" ]; then
  echo "Javadoc output not found in '$SOURCE_DIR'. Run 'mvn javadoc:javadoc' first."
  exit 1
fi

# Create the destination directory if it doesn't exist
mkdir -p "$DEST_DIR"

# Copy the contents from apidocs to doc folder
cp -r "$SOURCE_DIR/"* "$DEST_DIR/"

echo "Javadoc successfully copied to '$DEST_DIR/'"