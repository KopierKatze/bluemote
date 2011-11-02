
doc.pdf: doc.tex tudbeamer.cls Makefile
	mkdir -p build
	pdflatex -output-directory build doc.tex
	rm -f doc.pdf
	ln -s build/doc.pdf .

clean:
	rm -rf doc.pdf build

