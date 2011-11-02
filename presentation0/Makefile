
doc.pdf: doc.tex tudbeamer.cls
	mkdir -p build
	pdflatex -output-directory build doc.tex
	ln -fs build/doc.pdf .

clean:
	rm -rf doc.pdf build

