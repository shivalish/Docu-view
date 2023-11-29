import React, { useEffect, useState } from 'react';

const ImagePreview = ({ fileId }) => {
    const getStoredImage = () => {
        return localStorage.getItem(`filePreview${fileId}`);
    };

    // Check for image and re-render if it appears for 20 seconds before giving up
    const [renderedImg, setRenderedImg] = useState(getStoredImage())

    useEffect(() => {
        let attempts = 0;
        let refreshInterval = setInterval(() => {
            const storedImage = getStoredImage();
            if (storedImage) {
                setRenderedImg(storedImage);
                clearInterval(refreshInterval);
            } else {
                attempts++;
                if (attempts > 20) {
                    clearInterval(refreshInterval);
                }
            }
        }, 1000);
    }, []);

    const storedImage = getStoredImage();

    if (renderedImg) {
        return <img src={storedImage} alt={`Preview for file ${fileId}`} />;
    } else {
        return (
            <div style={{ textAlign: 'center', marginTop: '20px' }}>
                Preview not available
            </div>
        );
    }
};

export default ImagePreview;
