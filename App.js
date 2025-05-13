import React, { useRef, useState } from 'react';

export default function ImageAveragingFilter() {
  const [imageURL, setImageURL] = useState(null);
  const [filteredURL, setFilteredURL] = useState(null);
  const imageRef = useRef();
  const canvasRef = useRef();

  const handleUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageURL(URL.createObjectURL(file));
      setFilteredURL(null); // reset previous filter
    }
  };

  const applyFilter = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    const image = imageRef.current;

    canvas.width = image.width;
    canvas.height = image.height;

    ctx.drawImage(image, 0, 0);
    let imageData = ctx.getImageData(0, 0, image.width, image.height);
    let data = imageData.data;
    let copy = new Uint8ClampedArray(data); // Copy to preserve original values

    const width = image.width;
    const height = image.height;

    const getPixel = (x, y, c) => {
      const i = (y * width + x) * 4 + c;
      return copy[i];
    };

    const setPixel = (x, y, c, value) => {
      const i = (y * width + x) * 4 + c;
      data[i] = value;
    };

    const kernelSize = 3;
    const offset = Math.floor(kernelSize / 2);

    for (let y = offset; y < height - offset; y++) {
      for (let x = offset; x < width - offset; x++) {
        for (let c = 0; c < 3; c++) { // RGB only
          let sum = 0;
          for (let dy = -offset; dy <= offset; dy++) {
            for (let dx = -offset; dx <= offset; dx++) {
              sum += getPixel(x + dx, y + dy, c);
            }
          }
          let avg = sum / (kernelSize * kernelSize);
          setPixel(x, y, c, avg);
        }
        data[(y * width + x) * 4 + 3] = 255; // Alpha
      }
    }

    ctx.putImageData(imageData, 0, 0);
    setFilteredURL(canvas.toDataURL());
  };

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">Image Pixel Averaging Filter</h1>
      <input type="file" accept="image/*" onChange={handleUpload} className="mb-4" />
      {imageURL && (
        <div className="space-y-2">
          <img
            ref={imageRef}
            src={imageURL}
            alt="Uploaded"
            className="max-w-xs rounded border"
            onLoad={applyFilter}
            hidden
          />
          <canvas ref={canvasRef} hidden />
          {filteredURL && (
            <div className="flex gap-4">
              <div>
                <h2 className="font-semibold">Original</h2>
                <img src={imageURL} alt="Original" className="max-w-xs rounded border" />
              </div>
              <div>
                <h2 className="font-semibold">Filtered</h2>
                <img src={filteredURL} alt="Filtered" className="max-w-xs rounded border" />
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
