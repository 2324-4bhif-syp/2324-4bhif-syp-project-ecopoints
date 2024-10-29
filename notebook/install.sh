#!/bin/bash

# Create notebook and its subdirectories
mkdir -p notebook/html_files
mkdir -p notebook/data

# Install required libraries
echo "Installing libraries..."
pip install pandas
pip install pydeck
pip install plotly
pip install jupyter-dash
pip install --upgrade notebook ipywidgets

# Confirmation message
echo "All required libraries have been installed, and directories have been created!"
