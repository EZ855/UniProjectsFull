import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button} from 'react-bootstrap'
import { Trash, Plus} from 'react-bootstrap-icons';
import './FormStyle.css'
import './SignupStyle.css'
import './CreateRecipe.css'
import { Editor } from "react-draft-wysiwyg";
import { EditorState } from "draft-js";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import Header from '../components/Header';

function CreateRecipe () {
    const navigate = useNavigate();
    const [recipeTitle, setRecipeTitle] = React.useState('');
    const [mealType, setMealType] = React.useState('Breakfast');
    const [tags, setTags] = React.useState(['Italian'])
    const [equipments, setEquipments] = React.useState('');
    const [ingredientList, setIngredientList] = React.useState([
        { name: '', quantity: '', units: '' }
      ]);
    const [method, setMethod] = React.useState(() => EditorState.createEmpty());
    const [fileSelected, setFileSelected] = React.useState<File>();

    function landing () {
        navigate('/')
    }
    function clickCreate (e: React.SyntheticEvent<EventTarget>) {
        // Converting quantity from string to number
        // Checks also for empty ingredients
        let ingredientListFinal: { name: string; quantity: number; units: string; }[] = [];
        ingredientList.forEach((element) => {
            if (element.name === '' || !(Number(element.quantity) > 0) || element.units === '') {
                alert('Please fill in all required fields.')
                return
            }
            let add = {
                name: element.name,
                quantity: Number(element.quantity), 
                units: element.units
            }
            ingredientListFinal.push(add);
        });

        if (method.getCurrentContent().getPlainText('\u0001')==='') {
            alert('Please fill in all required fields.')
            return
        }
        
        console.log('sending stringified=', JSON.stringify({
            token: localStorage.token,
            recipe: {
                name: recipeTitle,
                meal_type: mealType,
                tags: tags,
                equipments: equipments,
                ingredients: ingredientListFinal,
                methods: method.getCurrentContent().getPlainText('\u0001'),
                photo: "image.jpeg"
            }
        }))
    
        fetch('http://localhost:8080/recipes/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: localStorage.token,
                recipe: {
                    name: recipeTitle,
                    meal_type: mealType,
                    tags: tags,
                    equipments: equipments,
                    ingredients: ingredientListFinal,
                    methods: method.getCurrentContent().getPlainText('\u0001'),
                    photo: ''
                }
            })
        })
        .then(r => {
            if (r.status === 200) {
                console.log('here')
                return r.json();
            }
            else {
                console.log('here!!')
                return "Server error"
            }
        })
        .then(success => {
            console.log(success)
            if (success === 'Server error') {
                alert("Server error")
            }
            else{
                navigate('/recipecreated')
            }
        })
    }
     

    // Ingredients related functions
    const handleFormChange = (index: number, event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault();
        let data = [...ingredientList];
        if (event.target.name === 'name' || event.target.name === 'quantity' || event.target.name === 'units') {
            data[index][event.target.name] = event.target.value;
            setIngredientList(data);
        }   
    }

    const addFields = () => {
        let newfield = { name: '', quantity: '', units: ''}

        setIngredientList([...ingredientList, newfield])
    }
    
    const removeFields = (index: number) => {
        let data = [...ingredientList];
        data.splice(index, 1)
        setIngredientList(data)
    }
    

    // Tags related functions
    function handleKeyDown(e : any){
        if(e.key !== 'Enter') return
        const value = e.target.value
        if(!value.trim()) return
        setTags([...tags, value])
        e.target.value = ''
    }

    function removeTag(index: number){
        setTags(tags.filter((el, i) => i !== index))
    }





      return <>
      <Header></Header>
      <div className='formWrapper'>
        <div className='createRecipeBox'>
            <h1 className={'logTitle'}>Create Recipe</h1>
            <p/>
            <p/>
            <Form className=''>
                <p className='logTitle'>Recipe Title*</p>
                <Form.Group controlId='recipeTitle'>
                    <Form.Control value = {recipeTitle} onChange={e => setRecipeTitle(e.target.value)} placeholder='Enter title'/>
                </Form.Group>
                <p/>
                <Form.Group controlId='mealType'>
                <p className='logTitle'>Meal Type</p>
                {['radio'].map((type) => (
                    <div key={`inline-${type}`} className="mb-3">
                    <Form.Check
                        inline
                        type='radio'
                        label="Breakfast"
                        name="group1"
                        defaultChecked
                        id={`inline-${type}-1`}
                        onChange={event => setMealType('Breakfast')}
                    />
                    <Form.Check
                        inline
                        type='radio'
                        label="Lunch"
                        name="group1"
                        id={`inline-${type}-2`}
                        onChange={event => setMealType('Lunch')}
                    />
                    <Form.Check
                        inline
                        type='radio'
                        name="group1"
                        label="Dinner"
                        id={`inline-${type}-3`}
                        onChange={event => setMealType('Dinner')}
                    />
                    <Form.Check
                        inline
                        type='radio'
                        name="group1"
                        label="Snack"
                        id={`inline-${type}-4`}
                        onChange={event => setMealType('Snack')}
                    />
                    </div>
                ))}
                </Form.Group>
                <p/>
                <p className='logTitle'>Ingredients</p>
                <Form.Group>
                {ingredientList.map((input, index) => {
                    return (
                        <div>
                        <div key={index} className='crIngredientContainer'>
                            <input
                                className='crBoxBorder crFormSize'
                                name='name'
                                placeholder=' Ingredient name'
                                type= 'text'
                                value={input.name}
                                onChange={event => handleFormChange(index, event)}
                            />
                            &nbsp;
                            <button className='crBoxBorder crFormSize deleteButton' type= 'button' onClick={() => removeFields(index)}><Trash></Trash></button>
                            <input
                                className='crBoxBorder crFormSize'
                                name='quantity'
                                placeholder=' Ingredient quantity'
                                type= 'number'
                                min= '0'
                                value={input.quantity}
                                onChange={event => handleFormChange(index, event)}
                            />
                            &nbsp;
                            <input style={{width: '50px'}}
                                className='crBoxBorder crFormSize'
                                name='units'
                                type= 'text'
                                placeholder=' Units'
                                value={input.units}
                                onChange={event => handleFormChange(index, event)}
                            />
                        </div>
                        <hr></hr>
                        </div>
                        )
                })}
                </Form.Group>
                <button className='crBoxBorder crFormSize' type= 'button' onClick={addFields}>&nbsp;&nbsp;Add another ingredient <Plus></Plus>&nbsp;&nbsp;</button>
                <p/>
                <p className='logTitle'>Method</p>
                 <div className='methodBox'>
                    <Editor
                    editorState={method}
                    onEditorStateChange={setMethod}
                    />
                </div>
                <p/>
                <p className='logTitle'>Tags</p>
                <Form.Group controlId='tags'>
                <div className="tags-input-container">
                    { tags.map((tag, index) => (
                        <div className="tag-item" key={index}>
                            <span className="text">{tag}</span>
                            <span className="close" onClick={() => removeTag(index)}>&times;</span>
                        </div>
                    )) }
                    <input onKeyDown={handleKeyDown} type="text" className="tags-input" placeholder="Type your #tags here!" />
                </div>
                </Form.Group>
                <p/>
                <Form.Group controlId='mealType'>
                <label>Upload Recipe Image</label>
                    <div>
                        <input type="file" name="myImage" accept="image/png, image/jpeg, image/jpg" onChange={e => {
                            console.log(e.target.files);
                            if (!e.target.files || e.target.files.length === 0) {
                                console.error("Select a file")
                            }
                            else {
                                setFileSelected(e.target.files[0])
                            }
                        }}/>
                    </div>
                </Form.Group>
                <br />
                <br />
                <Button onClick={clickCreate} className={'other_button submitButton'}>
                Create recipe
                </Button>
            </Form>
            </div>
        </div>
      </>;
    }
    
    export default CreateRecipe;